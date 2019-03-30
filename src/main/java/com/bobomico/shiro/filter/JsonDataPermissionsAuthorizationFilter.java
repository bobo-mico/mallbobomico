package com.bobomico.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.bobomico.bo.UserLoginRetryInfo;
import com.bobomico.common.ServerResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @ClassName: com.bobomico.shiro.filter.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/27  18:34
 * @Description: 配置文件中的鉴权过滤器
 *                  重写的主要原因是对无权访问的情况做出json回应
 * @version:
 */
public class JsonDataPermissionsAuthorizationFilter extends AuthorizationFilter {

    private static final Logger logger = Logger.getLogger("JsonDataPermissionsAuthorizationFilter");

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 不是ajax请求
        // if (!"XMLHttpRequest".equalsIgnoreCase((httpServletRequest).getHeader("X-Requested-With"))) {  // 不是ajax请求
        //     setFailureAttribute(request, e);
        //     return true;
        // }

        try {
            logger.info("过滤器 - 用户无权访问");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");

            ServerResponse<String> serverResponse = ServerResponse.createByErrorMessage("您无权访问");

            httpServletResponse.getWriter().write(JSONObject.toJSONString(serverResponse));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return false;
    }

    /**
     * 是否全部授权
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws IOException
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else {
                if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
        }
        return isPermitted;
    }
}