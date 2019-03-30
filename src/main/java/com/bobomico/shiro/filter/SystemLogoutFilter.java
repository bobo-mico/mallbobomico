package com.bobomico.shiro.filter;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @ClassName: com.bobomico.shiro.filter.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/27  17:09
 * @Description: 注销过滤器
 * @version:
 */
public class SystemLogoutFilter extends LogoutFilter {

    @Override
    public void setRedirectUrl(String name) {
        // 自定义跳转链接
        super.setName(name);
    }

    /**
     * 注销过滤器
     * @param request
     * @param response
     * @return false 表示不执行后续的过滤器，直接返回跳转到配置页面
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 在这里执行退出系统前需要清空的数据
        Subject subject = getSubject(request, response);

        String redirectUrl = getRedirectUrl(request, response, subject);

        setRedirectUrl("/index.html");

        System.out.println("注销后跳转地址: " + getRedirectUrl());

        try {
            subject.logout();
        } catch (SessionException ise) {
            ise.printStackTrace();
        }

        issueRedirect(request, response, "/index.html");

        return false;
    }

}
