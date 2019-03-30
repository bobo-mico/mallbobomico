package com.bobomico.exception;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: com.bobomico.controller.portal.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  23:29
 * @Description: web工具类
 * @version:
 */
public class WebUtilsPro {

    /**
     * ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}