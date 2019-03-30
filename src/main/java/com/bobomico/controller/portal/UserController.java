package com.bobomico.controller.portal;

import com.bobomico.common.Const;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.vo.UserLoginVO;
import com.bobomico.dao.po.SysUserInf;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * Shiro的五大权限注解及含义
 * RequiresAuthentication: 使用该注解标注的类，实例，方法在访问或调用时，当前Subject必须在当前session中已经过认证。
 * RequiresGuest: 使用该注解标注的类，实例，方法在访问或调用时，当前Subject可以是“gust”身份，不需要经过认证或者在原先的session中存在记录。
 * RequiresPermissions: 当前Subject需要拥有某些特定的权限时，才能执行被该注解标注的方法。如果当前Subject不具有这样的权限，则方法不会被执行。
 * RequiresRoles: 当前Subject必须拥有所有指定的角色时，才能访问被该注解标注的方法。该方法会抛出AuthorizationException异常。
 * RequiresUser: 当前Subject必须是应用的用户，才能访问或调用被该注解标注的类，实例，方法。
 */

/**
 * @ClassName: com.ccfit.po.mybatismain
 * @Author: Lion
 * @Date: 2018/6/20  8:13
 * @Description: 控制器就是指挥塔 不包含业务逻辑 只存在调用逻辑
 * @version: beta
 */
@RestController
@RequestMapping("/api/account")
public class UserController{

    @Autowired
    private IUserService iUserService;

    /**
     * 用户注册
     * @param userLoginVO
     * @return
     */
    @PostMapping("/register.do")
    public ServerResponse<String> register(UserLoginVO userLoginVO) {
        return iUserService.register(userLoginVO);
    }

    /**
     * 密码更新 - 主动修改通道
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping("/reset_password.do")
    @RequiresPermissions("item:update:update")
    public ServerResponse<String> resetPassword(
            String passwordOld, String passwordNew, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sysUserLogin = (SysUserLogin) getUserInfo(session.getId(), request, response);
        if(sysUserLogin == null){
            return ServerResponse.createByErrorMessage("获取用户信息异常");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, sysUserLogin.getSysUserId());
    }

    /**
     * 获取用户详细信息
     * @param session
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/get_user_inf.do")
    @RequiresAuthentication
    public ServerResponse<SysUserInf> getUserInf(
            HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sysUserLogin = (SysUserLogin) getUserInfo(session.getId(), request, response);
        if(sysUserLogin == null){
            return ServerResponse.createByErrorMessage("获取用户信息异常");
        }
        return iUserService.getInformation(sysUserLogin.getSysUserId());
    }

    /**
     * 获取密码提示问题
     * @param userId
     * @return
     */
    @PostMapping("forget_get_question.do")
    @RequiresAuthentication
    public ServerResponse<String> forgetGetQuestion(Integer userId){
        return iUserService.selectQuestion(userId);
    }

    /**
     * 验证答案
     * @param userId
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("forget_check_answer.do")
    @RequiresAuthentication
    public ServerResponse<String> forgetCheckAnswer(Integer userId, String question, String answer){
        return iUserService.checkAnswer(userId, question, answer);
    }

    /**
     * 重置密码 - 忘记密码通道
     * @param userId
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("forget_reset_password.do")
    @RequiresAuthentication
    public ServerResponse<String> forgetRestPassword(Integer userId, String passwordNew, String forgetToken){
        return iUserService.forgetResetPassword(userId, passwordNew, forgetToken);
    }

    /**
     * 设置及更新个人资料
     * @param session
     * @param request
     * @return
     */
    @PostMapping("update_information.do")
    @RequiresAuthentication
    public ServerResponse<SysUserInf> update_information(
            HttpSession session, SysUserInf sysUserInf, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sysUserLogin = (SysUserLogin) getUserInfo(session.getId(), request, response);
        sysUserInf.setSysUserId(sysUserLogin.getSysUserId());
        sysUserInf.setSysUserInfId(sysUserLogin.getSysUserId());
        ServerResponse<SysUserInf> resp = iUserService.updateInformation(sysUserInf);
        if(resp.isSuccess()){
            // todo 这里可以通过shiro的sessionDAO上传到缓存中
            session.setAttribute(Const.CURRENT_USER, resp.getData());
        }
        return resp;
    }

    /**
     * 获取当前用户
     * @param sessionID
     * @param request
     * @param response
     * @return
     */
    public Object getUserInfo(String sessionID, HttpServletRequest request, HttpServletResponse response){
        SessionKey key = new WebSessionKey(sessionID,request,response);
        try{
            Session session = SecurityUtils.getSecurityManager().getSession(key);
            SimplePrincipalCollection simplePrincipalCollection =
                    (SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            return simplePrincipalCollection.getPrimaryPrincipal();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 认证判断 几乎不会用到
     * @param sessionID
     * @param request
     * @param response
     * @return
     */
    public boolean isAuthenticated(String sessionID, HttpServletRequest request, HttpServletResponse response){
        boolean status = false;
        SessionKey key = new WebSessionKey(sessionID,request,response);
        try{
            Session session = SecurityUtils.getSecurityManager().getSession(key);
            Object obj = session.getAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
            if(obj != null){
                status = (Boolean) obj;
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            Session session = null;
            Object obj = null;
        }
        return status;
    }
}