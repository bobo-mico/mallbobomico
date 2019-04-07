package com.bobomico.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.bobomico.common.ResponseCode;
import com.bobomico.common.ServerResponse;
import com.bobomico.shiro.common.AjaxTool;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @ClassName: com.bobomico.shiro.filter.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/27  17:09
 * @Description: 注销过滤器 返回Json数据
 * @version:
 */
public class JsonDataLogoutFilter extends LogoutFilter {

    @Autowired
    private AjaxTool ajaxTool;

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

        // 在这里执行退出系统前需要清空的数据
        Subject subject = getSubject(request, response);

        // String redirectUrl = getRedirectUrl(request, response, subject);

        try {
            System.out.println("正在执行注销");
            subject.logout();
        } catch (SessionException ise) {
            ise.printStackTrace();
        }

        // 准备Json数据
        ServerResponse meg = ServerResponse.createByErrorCodeMessage(
                ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSONObject.toJSONString(meg));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        // issueRedirect(request, response, "/dist/view/user-login.html");
        return false;
    }
}