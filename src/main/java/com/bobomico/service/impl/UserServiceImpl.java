package com.bobomico.service.impl;

import com.bobomico.common.Const;
import com.bobomico.common.ServerResponse;
import com.bobomico.common.TokenCache;
import com.bobomico.controller.vo.UserLoginVo;
import com.bobomico.dao.SysPermissionMapperCustom;
import com.bobomico.dao.SysQuestionAnswerMapper;
import com.bobomico.dao.SysUserInfMapper;
import com.bobomico.dao.SysUserLoginMapper;
import com.bobomico.dao.po.SysPermission;
import com.bobomico.dao.po.SysQuestionAnswer;
import com.bobomico.dao.po.SysUserInf;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.pojo.Question;
import com.bobomico.pojo.RegisterUser;
import com.bobomico.service.IUserService;
import com.bobomico.shiro.token.EmailPasswordToken;
import com.bobomico.shiro.token.PhonePasswordToken;
import com.bobomico.util.SnowFlake;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private RegisterUser registerUser;

    @Autowired
    private SnowFlake SnowFlake;

    // todo 添加到自定义的dao中
    @Autowired
    private SysUserLoginMapper sysUserLoginMapper;

    @Autowired
    private SysQuestionAnswerMapper sysQuestionAnswerMapper;

    @Autowired
	private SysPermissionMapperCustom sysPermissionMapperCustom;

    @Autowired
    private SysUserInfMapper sysUserInfMapper;

    /**
     * 注册 支持三种注册通道
     * @param userLoginVO
     * @return
     */
    @Override
    public ServerResponse<String> register(UserLoginVo userLoginVO){
        String type = userLoginVO.getType();
        ServerResponse validResponse;

        switch (type){
            case Const.EMAIL:
                String loginEmail = userLoginVO.getLoginEmail();
                validResponse = this.checkValid(loginEmail, Const.EMAIL);
                if(!validResponse.isSuccess()){
                    return validResponse;
                }
                registerUser.getSysUserLogin().setLoginEmail(loginEmail);
                break;
            case Const.PHONE:
                return ServerResponse.createByErrorMessage("暂不支持手机注册方式");
            case Const.USERNAME:
                String loginName = userLoginVO.getLoginName();
                validResponse = this.checkValid(loginName, Const.USERNAME);
                if(!validResponse.isSuccess()){
                    return validResponse;
                }
                registerUser.getSysUserLogin().setLoginName(loginName);
                break;
            default:
                return ServerResponse.createByErrorMessage("请选择正确的方式注册");
        }

        // 记录原始盐
        String originSalt = SnowFlake.getId();

        // 准备密码
        ByteSource salt = ByteSource.Util.bytes(originSalt);
        String md5Password = new SimpleHash(
                Const.shiro.HASHALGORITHM, userLoginVO.getPassword(), salt, Const.shiro.HASHITERATIONS).toString();

        // 准备registerUser数据
        SysUserLogin sysUserLogin = registerUser.getSysUserLogin();
        sysUserLogin.setSalt(originSalt);
        sysUserLogin.setPassword(md5Password);
        sysUserLogin.setUserStats((byte)Const.userStatus.NOTACTIVE);
        registerUser.setSysUserLogin(sysUserLogin);
        sysUserLogin = null; // 释放对象

        // 执行持久化操作
        sysUserLoginMapper.register(registerUser);
        int resultCount = registerUser.getResultCount();
        if(resultCount == 0){   // 返回数据库的操作数
            return ServerResponse.createByErrorMessage("注册失败");
        }

        // 如果是邮箱注册
        if(Const.EMAIL.equals(type)){
            // 发送激活邮件
            // String activeCode = EmailUtils.sendAccountActivateEmail(registerUser.getSysUserLogin().getLoginEmail());
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


    /**
     * 密码重置 - 主动
     * @param passwordOld
     * @param passwordNew
     * @param sysUserId
     * @return
     */
    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, Integer sysUserId) {
        // 先对数据进行简单校验
        if(!org.apache.commons.lang3.StringUtils.isNotBlank(passwordNew)){
            return ServerResponse.createByErrorMessage("您的新密码为空");
        }
        if(!org.apache.commons.lang3.StringUtils.isNotBlank(passwordOld)){
            return ServerResponse.createByErrorMessage("您的旧密码为空");
        }
        if(passwordNew.equals(passwordOld)){
            return ServerResponse.createByErrorMessage("您的新密码和旧密码一样");
        }

        // 防止横向越权 校验用户的旧密码 一定要是当前用户
        SysUserLogin sysUserLogin = sysUserLoginMapper.selectByPrimaryKey(sysUserId);

        // 旧密码校验
        String md5PasswordOld = new SimpleHash(
                Const.shiro.HASHALGORITHM, passwordOld, ByteSource.Util.bytes(sysUserLogin.getSalt()), Const.shiro.HASHITERATIONS).toString();
        if(!sysUserLogin.getPassword().equals(md5PasswordOld)){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        // 新生成原始盐
        String originSalt = SnowFlake.getId();

        // 新密码准备
        String md5PasswordNew = new SimpleHash(
                Const.shiro.HASHALGORITHM, passwordNew, ByteSource.Util.bytes(originSalt), Const.shiro.HASHITERATIONS).toString();
        sysUserLogin.setPassword(md5PasswordNew);
        sysUserLogin.setSalt(originSalt);
        int resultCount = sysUserLoginMapper.resetPassword(sysUserLogin);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    /**
     * 校验 防止数据接口扫描攻击
     * @param subject
     * @param type
     * @return
     */
    private ServerResponse<String> checkValid(String subject, String type) {
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            if(Const.USERNAME.equals(type)) {
                // 检查同名username
                int resultCount = sysUserLoginMapper.checkUsername(subject);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已经存在");
                }
            }else if (Const.EMAIL.equals(type)) {
                int resultCount = sysUserLoginMapper.checkEmail(subject);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已经存在");
                }
            }else if(Const.USERID.equals(type)) {
                int userId = 0;
                try{
                    userId = Integer.valueOf(subject).intValue();
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                int resultCount = sysUserLoginMapper.checkUserId(userId);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户已经存在");
                }
            }
            // todo 后续追加手机注册逻辑
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 根据不同的token类型进行不同的数据库查询
     * @param token 自定义
     * @return
     */
    @Override
    public SysUserLogin findSysUserByTokenType(AuthenticationToken token) {
        SysUserLogin sysUserLogin = null;

        String principal = (String) token.getPrincipal();

        // todo 以下的查询mapper最好追加id判断
        if(token instanceof EmailPasswordToken) {
            sysUserLogin = sysUserLoginMapper.selectLoginForEmail(principal);
        }else if(token instanceof UsernamePasswordToken) {
            sysUserLogin = sysUserLoginMapper.selectLoginForUsername(principal);
        }else if(token instanceof PhonePasswordToken) {
            sysUserLogin = sysUserLoginMapper.selectLoginForPhone(principal);
        }
        return sysUserLogin;
    }

    /**
     * 修改用户锁定状态
     * @param sysUserLogin
     * @return
     */
    @Override
    public int updateUserStatus(SysUserLogin sysUserLogin) {
        return sysUserLoginMapper.updateByPrimaryKeySelective(sysUserLogin);
    }

    /**
     * 根据用户id查询权限范围的菜单
     * @param userId
     * @return
     * @throws Exception
     */
	@Override
	public List<SysPermission> findMenuListByUserId(Integer userId) throws Exception {
		return sysPermissionMapperCustom.findMenuListByUserId(userId);
	}

    /**
     * 根据用户id查询权限范围的url
     * @param userId
     * @return
     * @throws Exception
     */
	@Override
	public List<SysPermission> findPermissionListByUserId(Integer userId) throws Exception {
		return sysPermissionMapperCustom.findPermissionListByUserId(userId);
	}

    /**
     * 获取密码提示问题
     * @param userId
     * @return
     */
    @Override
    public ServerResponse selectQuestion(Integer userId) {
        ServerResponse validResponse = this.checkValid(String.valueOf(userId), Const.USERID);
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        List<Question> questions = sysQuestionAnswerMapper.selectByUserId(userId);
        if(questions != null){
            return ServerResponse.createBySuccess(questions);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    /**
     * 验证答案
     * @param userId
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<String> checkAnswer(Integer userId, String question, String answer) {
        int resultCount = sysQuestionAnswerMapper.checkAnswer(userId, question, answer);
        // 说明问题及问题答案是这个用户的且正确
        if(resultCount > 0){
            // 通过UUID算法获取一个密改token
            String forgetToken = UUID.randomUUID().toString();
            // 设置进缓存
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + userId, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    /**
     * 重置密码 - 忘记密码通道
     * @param userId
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<String> forgetResetPassword(Integer userId, String passwordNew, String forgetToken) {
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误, 改密token丢失");
        }
        ServerResponse validResponse = this.checkValid(String.valueOf(userId), Const.USERID);
        // 防止横向越权
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + userId);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("改密token无效或过期");
        }
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            // 记录原始盐
            String originSalt = SnowFlake.getId();
            // 准备密码
            ByteSource salt = ByteSource.Util.bytes(originSalt);
            String md5Password = new SimpleHash(
                    Const.shiro.HASHALGORITHM, passwordNew, salt, Const.shiro.HASHITERATIONS).toString();
            // 准备持久化对象
            SysUserLogin sysUserLogin = new SysUserLogin(userId, originSalt, md5Password);
            int rowCount = sysUserLoginMapper.resetPassword(sysUserLogin);
            if(rowCount > 0){   // 生效行数判断
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误, 请重新获取重置密码的token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 设置及更新用户资料
     * @param sysUserInf
     * @return
     */
    @Override
    public ServerResponse<SysUserInf> updateInformation(SysUserInf sysUserInf) {
        int updateCount = sysUserInfMapper.updateByPrimaryKeySelective(sysUserInf);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("更新个人信息成功", sysUserInf);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    /**
     * 获取当前用户详细信息
     * @param userId
     * @return
     */
    public ServerResponse<SysUserInf> getInformation(Integer userId){
        SysUserInf user = sysUserInfMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        // todo 通过sessionDAO将查询到的用户信息设置到缓存中
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 设置密码提示问题及答案
     * @param questions
     * @return
     */
    public ServerResponse<String> insertQuestionAnswer(List<SysQuestionAnswer> questions){
        // 先过滤大于4的问题编号
        questions = questions.stream()
                .filter(x -> x.getSysUserId() < 4)
                .collect(Collectors.toList());
        // 插入问题
        questions.stream().forEach(sysQuestionAnswerMapper::createSelective);
        return ServerResponse.createBySuccessMessage("密码问题已更新");
    }
}