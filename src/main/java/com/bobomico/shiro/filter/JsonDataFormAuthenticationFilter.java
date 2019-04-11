package com.bobomico.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bobomico.bo.UserLoginRetryInfo;
import com.bobomico.common.Const;
import com.bobomico.common.ServerResponse;
import com.bobomico.shiro.cache.PasswordRetryCache;
import com.bobomico.shiro.common.AjaxTool;
import com.bobomico.shiro.token.EmailPasswordToken;
import com.bobomico.shiro.token.PhonePasswordToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/*
Shiro提供了三个默认实现：
    DefaultSessionManager：DefaultSecurityManager使用的默认实现，用于JavaSE环境；
    ServletContainerSessionManager：DefaultWebSecurityManager使用的默认实现，用于Web环境，其直接使用Servlet容器的会话；
    DefaultWebSessionManager：用于Web环境的实现，可以替代ServletContainerSessionManager，自己维护着会话，直接废弃了Servlet容器的会话管理。
*/

/**
 * @ClassName: com.ccfit.shiro.customerFilter.ssms
 * @Author: Lion
 * @Date: 2019/2/19  15:39
 * @Description: 重写支持Json解析的拦截器
 *                代码需要重构 还不够完善
 * @version: beta
 */
@Component
public class JsonDataFormAuthenticationFilter extends FormAuthenticationFilter{

    private static final Logger log = LoggerFactory.getLogger(JsonDataFormAuthenticationFilter.class);

    @Autowired
    private AjaxTool ajaxTool;

    @Autowired
    private PasswordRetryCache passwordRetryCache;

    /**
     * 防止重复登录
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if(isLoginRequest(request, response) && isLoginSubmission(request, response)){
            if(log.isTraceEnabled()){
                log.trace("Login submission detected. Attempting to execute login.");
            }
            return false;
        }
        return super.isAccessAllowed(request, response, mappedValue);
    }

    /**
     * 执行登录时调用的函数
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // // 通过请求直接获取request中包含的所有的cookie
        // Cookie[] cookies = ((HttpServletRequest)request).getCookies();
        // // 遍历所有的cookie,然后根据cookie的key值来获取value值
        // if (cookies!=null) {
        //     for (Cookie cookie : cookies) {
        //         System.out.println(cookie.getName());
        //         System.out.println(cookie.getValue());
        //     }
        // }

        // 是否是Ajax请求
        if(ajaxTool.isAjax(request)){
            try {
                Map<String, Object> _xr = ajaxTool.refactoringRequest(request, response);
                request = (ServletRequest) _xr.get("request");
                response = (ServletResponse) _xr.get("response");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }

        // 待认证用户
        AuthenticationToken token = createToken(request, response);

        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                    "must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        }
        try {
            Subject subject = getSubject(request, response);
            /**
             * think about
             *      先前的代码在提交之前保存了session状态 认证之后注销了session 导致session中保存的
             *      两个关键数据丢失 AUTHENTICATED_SESSION_KEY PRINCIPALS_SESSION_KEY
             *      直接导致之后的访问都无法保存登录状态 而控制器中调用getUserInfo isAuthenticated更是导致空指针异常
             *      现将逻辑修正为 在执行认证之后 保存状态 注销session 复制状态 问题解决
             */
            System.out.println("认证前SESSION ID = " + SecurityUtils.getSubject().getSession().getId());

            // 提交认证 如果错误会抛出异常并执行catch
            subject.login(token);

            // 防止Session fixation
            Session session = subject.getSession();
            final LinkedHashMap<Object, Object> attributes = new LinkedHashMap();
            final Collection<Object> keys = session.getAttributeKeys();
            for (Object key : keys) {
                final Object value = session.getAttribute(key);
                if (value != null) {
                    attributes.put(key, value);
                }
            }
            // 状态保存后注销当前session
            session.stop();
            // 获取新的session
            session = SecurityUtils.getSubject().getSession();
            // 复制session数据
            for (final Object key : attributes.keySet()){
                session.setAttribute(key, attributes.get(key));
            }

            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * @title: 认证成功后
     * @Description: 主要是针对登入成功的处理方法,对于请求头是AJAX的返回JSON字符串
     *                  ajax无法处理重定向,这时要重写shiro成功时的处理方法
     *                  认证成功时需要添加会话管理的功能
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // // 获取session
        // Session session = subject.getSession(false);
        // if(session != null){
        //     boolean flag = (boolean) session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
        //     System.out.println(flag);
        // }

        System.out.println("认证成功时SESSION ID = " + SecurityUtils.getSubject().getSession().getId());
        System.out.println("用户名：" + SecurityUtils.getSubject().getPrincipal());
        System.out.println("HOST：" + SecurityUtils.getSubject().getSession().getHost());
        System.out.println("TIMEOUT ：" + SecurityUtils.getSubject().getSession().getTimeout());
        System.out.println("START：" + SecurityUtils.getSubject().getSession().getStartTimestamp());
        System.out.println("LAST：" + SecurityUtils.getSubject().getSession().getLastAccessTime());

        // ajax请求的处理方法
        if (ajaxTool.isAjax(httpServletRequest)) {

            // todo
            // saveRequestAndRedirectToLogin(request, response);
            // 非ajax请求重定向为登录页面
            // httpServletResponse.sendRedirect("/api2/account/login.do");

        }else{
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            ServerResponse result = ServerResponse.createBySuccess("登录成功", subject.getPrincipal());
            httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
        }
        return false;
    }

    /**
     * 认证失败后
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        System.out.println("认证失败时SESSION ID = " + SecurityUtils.getSubject().getSession().getId());
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

       // 不是ajax请求
       // if (!"XMLHttpRequest".equalsIgnoreCase((httpServletRequest).getHeader("X-Requested-With"))) {  // 不是ajax请求
       //     setFailureAttribute(request, e);
       //     return true;
       // }

        try {
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");

            ServerResponse<String> serverResponse;
            // 解析错误信息
            String message = e.getClass().getSimpleName();
            if ("IncorrectCredentialsException".equals(message)) {
                serverResponse = ServerResponse.createByErrorMessage("密码错误" );
            }else if("ExcessiveAttemptsException".equals(message)) {
                System.out.println(message);
                UserLoginRetryInfo userLoginRetryInfo =
                        passwordRetryCache.getPasswordRetryCache().get((String)token.getPrincipal());
                int retryCount = userLoginRetryInfo.getAtomicInteger().get();
                System.out.println("密码错误" + retryCount);
                serverResponse = ServerResponse.createByErrorMessage("密码错误(您已连续输错" + retryCount + "次)" );
            }else if("UnknownAccountException".equals(message)) {
                System.out.println(message);
                serverResponse = ServerResponse.createByErrorMessage("账号未注册");
            }else if ("LockedAccountException".equals(message)) {
                System.out.println(message);
                serverResponse = ServerResponse.createByErrorMessage("账号被锁定");
            }else if("ExpiredCredentialsException ".equals(message)){
                serverResponse = ServerResponse.createByErrorMessage("过期的凭证");
            }else if("DisabledAccountException ".equals(message)){
                serverResponse = ServerResponse.createByErrorMessage("禁用的账号");
            }else{
                System.out.println("未知错误");
                serverResponse = ServerResponse.createByErrorMessage("未知错误");
            }
            httpServletResponse.getWriter().write(JSONObject.toJSONString(serverResponse));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return false;
    }

    // 拒绝访问
    // protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
    //     HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    //     return true;
    // }

    /**
     * 根据Type创建不同的token
     * @param request
     * @return
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // 获取ShiroHttpServletRequest中的json数据
        String reqBody = getParameters((HttpServletRequest) request);

        // JSON解析
        JSONObject json = JSON.parseObject(reqBody);
        String type = json.getString("type");
        String subject = json.getString("username");
        char[] password = json.getString("password").toCharArray();

        // 根据不同的type创建不同的token
        switch(type){
            case Const.EMAIL:
                return new EmailPasswordToken(subject, password);
            case Const.USERNAME:
                return new UsernamePasswordToken(subject, password);
            case Const.PHONE:
                return new PhonePasswordToken(subject, password);
            default:
                System.out.println("请选择正确的登录方式");
                return null;
        }
    }

    /**
     * 对ShiroHttpServletRequest进行处理 获取json字符串
     * @param request
     * @return
     */
    public String getParameters(HttpServletRequest request) {
        BufferedReader br;
        StringBuilder sb;
        String reqBody;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            reqBody = URLDecoder.decode(sb.toString(), "UTF-8");
            return reqBody;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR: Unable to resolve the request to json";
        }
    }
}