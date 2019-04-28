package com.bobomico.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: com.bobomico.controller.portal.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/31  3:02
 * @Description: Controller tools
 * @version:
 */
public class BaseController {

    /**
     * 获取当前用户 - 工具
     * @param request
     * @param response
     * @return
     */
    public Object getUserInfo(HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SessionKey key = new WebSessionKey(session.getId(), request, response);
        try{
            Session se = SecurityUtils.getSecurityManager().getSession(key);
            SimplePrincipalCollection simplePrincipalCollection =
                    (SimplePrincipalCollection)se.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
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
