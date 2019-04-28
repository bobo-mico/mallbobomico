package com.bobomico.shiro.common;

import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @ClassName: com.bobomico.shiro.common.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/2  13:39
 * @Description: shiro处理ajax请求的工具
 * @version:
 */
@Component
public class AjaxTool {

    /**
     * 封装request和response
     * @param servletRequest
     * @param servletResponse
     * @return 处理后的request和response
     * @throws IOException
     * @throws ServletException
     */
    public Map<String, Object> refactoringRequest(
            ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)servletResponse;
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        res.setContentType("textml;charset=UTF-8");
        res.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "0");
        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("XDomainRequestAllowed","1");
        Map<String, Object> _xr = new Hashtable();
        _xr.put("request", request);
        _xr.put("response", res);
        return _xr;
    }

    // 是否是ajax提交
    public boolean isAjax(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(header)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
