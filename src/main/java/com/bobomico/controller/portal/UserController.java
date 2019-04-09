package com.bobomico.controller.portal;

import com.bobomico.common.Const;
import com.bobomico.common.ResponseCode;
import com.bobomico.common.ServerResponse;
import com.bobomico.controller.BaseController;
import com.bobomico.controller.vo.UserLoginVo;
import com.bobomico.dao.po.SysQuestionAnswer;
import com.bobomico.dao.po.SysUserInf;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
 * @Description: 客户端 - 用户模块
 *                  控制器就是指挥塔 不包含业务逻辑 只存在调用逻辑
 * @version: beta
 */
@RestController
@RequestMapping("/user/")
public class UserController extends BaseController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户注册
     * @param userLoginVO
     * @return
     */
    @PostMapping("register.do")
    public ServerResponse<String> register(@RequestBody UserLoginVo userLoginVO) {
        return iUserService.register(userLoginVO);
    }

    /**
     * 密码更新 - 主动修改通道
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping("reset_password.do")
    @RequiresPermissions("user:create")
    public ServerResponse<String> resetPassword(
            String passwordOld, String passwordNew, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sysUserLogin = (SysUserLogin) getUserInfo(session, request, response);
        if(sysUserLogin == null){
            return ServerResponse.createByErrorMessage("获取用户信息异常");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, sysUserLogin.getSysUserId());
    }

    /**
     * 获取用户详细信息
     * @param session
     * @param response
     * @return
     */
    @GetMapping("get_user_inf.do")
    @RequiresAuthentication
    public ServerResponse<SysUserInf> getUserInf(
            HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sysUserLogin = (SysUserLogin) getUserInfo(session, request, response);
        if(sysUserLogin == null){
            return ServerResponse.createByErrorMessage("获取用户信息异常");
        }
        return iUserService.getInformation(sysUserLogin.getSysUserId());
    }

    /**
     * 获取密码提示问题
     * @return
     */
    @GetMapping("forget_get_question.do")
    @RequiresAuthentication
    public ServerResponse<String> forgetGetQuestion(
            HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iUserService.selectQuestion(sul.getSysUserId());
    }

    /**
     * 验证答案
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("forget_check_answer.do")
    @RequiresAuthentication
    public ServerResponse<String> forgetCheckAnswer(
            @RequestBody String question, @RequestBody String answer, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iUserService.checkAnswer(sul.getSysUserId(), question, answer);
    }

    /**
     * 重置密码 - 忘记密码通道
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("forget_reset_password.do")
    @RequiresAuthentication
    public ServerResponse<String> forgetRestPassword(
            @RequestBody String passwordNew, @RequestBody String forgetToken, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        return iUserService.forgetResetPassword(sul.getSysUserId(), passwordNew, forgetToken);
    }

    /**
     * 设置及更新个人资料
     * @param session
     * @param request
     * @return
     */
    @PostMapping("update_information.do")
    @RequiresAuthentication
    public ServerResponse<SysUserInf> updateInformation(
            SysUserInf sysUserInf, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sysUserLogin = (SysUserLogin) getUserInfo(session, request, response);
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
     * 设置密码提示问题及答案
     * @param questions 需提供question编号
     * @param session
     * @param request
     * @param response
     * @return
     */
    @PostMapping("create_question_answer.do")
    @RequiresAuthentication
    public ServerResponse<String> createQuestionAnswer(
            @RequestBody SysQuestionAnswer[] questions, HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        List<SysQuestionAnswer> qs = Arrays.asList(questions);
        qs = qs.stream().peek(x -> x.setSysUserId(sul.getSysUserId())).collect(Collectors.toList());
        return iUserService.insertQuestionAnswer(qs);
    }

    /**
     * 检查用户登录状态
     * @param session
     * @param request
     * @param response
     * @return
     */
    @PostMapping("check_login_info.do")
    @RequiresAuthentication
    public ServerResponse checkLoginInf(
            HttpSession session, HttpServletRequest request, HttpServletResponse response){
        SysUserLogin sul = (SysUserLogin) getUserInfo(session, request, response);
        if(sul != null){
            return ServerResponse.createBySuccess(sul);
        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
    }

    /**
     * 检查注册名是否存在
     * @param map username type 仅支持json
     * @return
     */
    @PostMapping("check_valid.do")
    public ServerResponse<String> checkValid(@RequestBody Map<String, String> map){
        return iUserService.checkValid(map.get("username"), map.get("type"));
    }
}