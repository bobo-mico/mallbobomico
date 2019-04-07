package com.bobomico.exception;

import com.alibaba.fastjson.JSONObject;
import com.bobomico.common.ResponseCode;
import com.bobomico.common.ServerResponse;
import org.apache.shiro.ShiroException;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.codec.CodecException;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.config.UnresolveableReferenceException;
import org.apache.shiro.crypto.CryptoException;
import org.apache.shiro.crypto.UnknownAlgorithmException;
import org.apache.shiro.dao.DataAccessException;
import org.apache.shiro.dao.InvalidResourceUsageException;
import org.apache.shiro.env.EnvironmentException;
import org.apache.shiro.env.RequiredTypeException;
import org.apache.shiro.io.SerializationException;
import org.apache.shiro.ldap.UnsupportedAuthenticationMechanismException;
import org.apache.shiro.session.*;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.support.DisabledSessionException;
import org.apache.shiro.util.UnavailableConstructorException;
import org.apache.shiro.util.UnknownClassException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * @ClassName: com.bobomico.exception.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  23:56
 * @Description: 全局异常处理接口
 *                  问题描述: shiro注解以aop的方式实现
 *                           这导致了鉴权抛出异常时 spring捕获异常后会强制渲染response
 *                           无法按需求返回json数据
 *                  解决思路: 我曾重写授权器，试图对异常时做出json响应，但之后spring会清理response
 *                              并重新渲染response。于是我又试图让授权器不抛出异常，但那样会影响鉴权的功能。
 *                              于是我陷入两难的境地，通过不断跟踪源码，终于想到，既然springmvc会对异常进行处理
 *                              那我能不能让springmvc将异常处理控制权交给我呢？于是就有了现在的解决方案。
 *                              不同注解的异常类型 请查阅源码 AopAllianceAnnotationsAuthorizingMethodInterceptor
 * @version:
 */
@ControllerAdvice
@ResponseBody
public class WebExceptionHandle {
    private static Logger logger = LoggerFactory.getLogger(WebExceptionHandle.class);

    /**
     * 认证异常
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
    public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户认证异常");
        if (WebUtilsPro.isAjaxRequest(request)) {
            ServerResponse meg = ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            writeJson(meg, response);
        } else {
            ServerResponse meg = ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
            writeJson(meg, response);
            // return "redirect:/login.html";
        }
        return null;
    }

    /**
     * 授权异常 UNAUTHORIZED 401 未经允许
     * todo 以后对非ajax做出对应的操作
     * @param request
     * @param response
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户授权异常");
        if (WebUtilsPro.isAjaxRequest(request)) {
            // 输出JSON
            ServerResponse meg = ServerResponse.createByErrorMessage("您无权进行该操作");
            writeJson(meg, response);
            return null;
        } else {
            // 输出JSON
            ServerResponse meg = ServerResponse.createByErrorMessage("您无权进行该操作");
            writeJson(meg, response);
            return null;
        }
    }

    /**
     * 返回Json
     * @param msg
     * @param response
     */
    private void writeJson(ServerResponse msg, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSONObject.toJSONString(msg));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @ExceptionHandler({
            // UnsupportedTokenException.class,
            // AccountException.class,
            // AuthenticationException.class,
            // ConcurrentAccessException.class,
            // CredentialsException.class,
            // DisabledAccountException.class,
            // ExcessiveAttemptsException.class,
            // ExpiredCredentialsException.class,
            // IncorrectCredentialsException.class,
            // LockedAccountException.class,

            // UnknownAccountException.class,
            // InvalidPermissionStringException.class,
            // AuthorizationException.class,
            // HostUnauthorizedException.class,

            // UnauthenticatedException.class,
            // UnauthorizedException.class,

            CacheException.class,
            CodecException.class,
            ConfigurationException.class,
            UnresolveableReferenceException.class,

            CryptoException.class,
            UnknownAlgorithmException.class,
            ShiroException.class,
            UnavailableSecurityManagerException.class,
            DataAccessException.class,

            InvalidResourceUsageException.class,
            EnvironmentException.class,
            RequiredTypeException.class,
            SerializationException.class,
            UnsupportedAuthenticationMechanismException.class,

            ExpiredSessionException.class,
            InvalidSessionException.class,
            SessionException.class,
            StoppedSessionException.class,
            UnknownSessionException.class,
            DisabledSessionException.class,
            ExecutionException.class,
            InstantiationException.class,
            UnavailableConstructorException.class,
            UnknownClassException.class
    })
    public String authorizationException2(HttpServletRequest request, HttpServletResponse response) {
        logger.info("用户授权异常");
        if (WebUtilsPro.isAjaxRequest(request)) {
            // 输出JSON
            ServerResponse meg = ServerResponse.createByErrorMessage("异常大礼包");
            writeJson(meg, response);
            return null;
        } else {
            // 输出JSON
            ServerResponse meg = ServerResponse.createByErrorMessage("异常大礼包");
            writeJson(meg, response);
            return null;
        }
    }
}

/**
 * Shiro-异常速查
 *  IncorrectCredentialsException 登录密码错误
 *  ExcessiveAttemptsException 登录失败次数过多
 *  LockedAccountException 帐号已被锁定
 *  DisabledAccountException 帐号已被禁用
 *  ExpiredCredentialsException 帐号已过期
 *  UnknownAccountException 帐号不存在
 */