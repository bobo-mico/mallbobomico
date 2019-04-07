webpackJsonp([9],{

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(150);


/***/ }),

/***/ 103:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: Rosen
	* @Date:   2017-05-17 17:04:32
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-04-03 05:16:03
	*/

	'use strict';

	var _mm = __webpack_require__(99);

	var _user = {
	    // 用户登录
	    login : function(userInfo, resolve, reject){
	        _mm.request({
	            // url     : _mm.getServerUrl('/user/login.do'),
	            url     : _mm.getServerUrl('/user/login.do'),
	            data    : userInfo,
	            method  : 'POST',
	            success : resolve,  // resolve 解析
	            error   : reject    // reject 驳回
	        });
	    },
	    // 检查用户名
	    checkUsername : function(username, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/check_valid.do'),
	            data    : {
	                'username' : username,
	                'type'     : 'email'
	            },
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 用户注册
	    register : function(userInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/register.do'),
	            data    : userInfo,
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 检查登录状态
	    checkLogin : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/check_login_info.do'),
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 获取用户密码提示问题
	    getQuestion : function(username, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/forget_get_question.do'),
	            data    : {
	                username : username
	            },
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 检查密码提示问题答案
	    checkAnswer : function(userInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/forget_check_answer.do'),
	            data    : userInfo,
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 重置密码
	    resetPassword : function(userInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/forget_reset_password.do'),
	            data    : userInfo,
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 获取用户信息
	    getUserInfo : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/get_information.do'),
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 更新个人信息
	    updateUserInfo : function(userInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/update_information.do'),
	            data    : userInfo,
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 登录状态下更新密码
	    updatePassword : function(userInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/reset_password.do'),
	            data    : userInfo,
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 注销
	    logout : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/user/logout.do'),
	            method  : 'POST',
	            success : resolve,
	            error   : reject
	        });
	    }
	}
	module.exports = _user;

/***/ }),

/***/ 130:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-14 13:18:11
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-02-14 13:18:55
	*/
	'use strict';
	__webpack_require__(131);

/***/ }),

/***/ 131:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }),

/***/ 150:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-19 23:58:05
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-04-03 05:31:17
	*/

	'use strict';
	__webpack_require__(151);
	__webpack_require__(130);
	var _user = __webpack_require__(103);
	var _mm = __webpack_require__(99);

	// 表单里的错误信息
	var formError = {
	    show : function(errMsg){
	        $('.error-item').show().find('.error-msg').text(errMsg);
	    },
	    hide : function(){
	        $('.error-item').hide().find('.error-msg').text('');
	    }
	};

	/* page逻辑部分 */
	var page = {
	    init: function(){
	        this.bindEvent();
	    },
	    bindEvent : function(){
	        var _this = this;
	        $('#username').blur(function(){
	            var username = $.trim($(this).val());
	            // 如果用户名为空 我们不做验证
	            if(!username){
	                return;
	            }
	            // 异步验证用户名是否存在
	            _user.checkUsername(username, function(res){
	                formError.hide();
	            }, function(errMsg){
	                 formError.show(errMsg);
	            });
	        });
	        // 注册按钮的点击
	        $('#submit').click(function(){
	            _this.submit();
	        });
	        // 如果按下回车 也进行提交     e 事件参数
	        $('.user-content').keyup(function(e){
	            // keyCode == 13 表示回车
	            if(e.keyCode === 13){
	                _this.submit();
	            }
	        });
	    },
	    // 提交表单
	    submit : function(){
	        var formData = {
	            username : $.trim($('#username').val()),
	            password : $.trim($('#password').val()),
	            passwordConfirm : $.trim($('#password-confirm').val()),
	            phone : $.trim($('#phone').val()),
	            email : $.trim($('#email').val()),
	            question : $.trim($('#question').val()),
	            answer : $.trim($('#answer').val())
	        },
	        // 表单验证结果
	        validateResult = this.formValidate(formData);
	        // 验证成功
	        if(validateResult.status){
	            _user.register(formData, function(res){    // 经过测试这个res没有用 我觉得这个应该删掉 不然容易误会
	                window.location.href = './result.html?type=register';
	            }, function(errMsg){
	                formError.show(errMsg);
	            });
	        }// 验证失败
	        else{
	            // 错误提示
	            formError.show(validateResult.msg);
	        }
	    },
	    // 表单字段的验证
	    formValidate : function(formData){
	        var result = {
	            status : false,
	            msg : ''
	        };
	        // 用户名是否为空
	        if( !_mm.validate(formData.username, 'require')){
	            result.msg = '用户名不能为空';
	            return result;
	        }
	        // 密码是否为空
	        if( !_mm.validate(formData.password, 'require')){
	            result.msg = '密码不能为空';
	            return result;
	        }
	        // 密码长度
	        if( formData.password.length < 6){
	            result.msg = '密码长度不能少于6位';
	            return result;
	        }
	        // 两次输入的密码是否一致
	        if( formData.password != formData.passwordConfirm ){
	            result.msg = '两次输入的密码不一致';
	            return result;
	        }
	        // 手机号验证
	        if( !_mm.validate(formData.phone, 'phone')){
	            result.msg = '请输入正确的手机号';
	            return result;
	        }
	        // 邮箱验证
	        if( !_mm.validate(formData.email, 'email')){
	            result.msg = '请输入正确的邮箱';
	            return result;
	        }
	        // 密码提示问题
	        if( !_mm.validate(formData.question, 'require')){
	            result.msg = '密码提示问题不能为空';
	            return result;
	        }
	        // 密码提示问题答案
	        if( !_mm.validate(formData.answer, 'require')){
	            result.msg = '密码提示问题的答案不能为空';
	            return result;
	        }
	        result.status = true;
	        result.msg = '验证通过';
	        return result;
	    }
	};

	$(function(){
	    page.init();
	});

/***/ }),

/***/ 151:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ })

});