webpackJsonp([7],{

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(144);


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

/***/ 144:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-22 11:42:12
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-02-22 13:23:37
	*/
	/*禁用严格模式*/
	/*'use strict';*/
	__webpack_require__(145);
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
	    // 保存每一步的数据
	    data : {
	        username : '',
	        question : '',
	        answer : '',
	        token : '',
	    },
	    init: function(){
	        this.onLoad();  // 显示第一步
	        this.bindEvent();
	    },
	    onLoad : function(){
	        this.loadStepUsername();
	    },
	    bindEvent : function(){
	        var _this = this;
	        // 输入用户名后下一步按钮的点击
	        $('#submit-username').click(function(){
	            var username = $.trim($('#username').val());
	            // 用户名存在
	            if(username){
	                _user.getQuestion(username, function(res){
	                    _this.data.username = username;
	                    _this.data.question = res;
	                    _this.loadStepQuestion();
	                }, function(errMsg){
	                    formError.show(errMsg);
	                });
	            }
	            // 用户名不存在
	            else{
	                formError.show('请输入用户名');
	            }
	        });
	        // 输入密码提示问题答案中的按钮点击
	        $('#submit-question').click(function(){
	            var answer = $.trim($('#answer').val());
	            // 密码提示问题答案存在
	            if(answer){
	                // 检查密码提示问题答案
	                _user.checkAnswer({
	                    username : _this.data.username,
	                    question : _this.data.question,
	                    answer : answer
	                }, function(res){
	                    _this.data.answer = answer;
	                    _this.data.token = res;
	                    _this.loadStepPassword();
	                }, function(errMsg){
	                    formError.show(errMsg);
	                });
	            }
	            // 用户名不存在
	            else{
	                formError.show('请输入密码提示问题答案');
	            }
	        });
	        // 输入新密码后的按钮点击
	        $('#submit-password').click(function(){
	            var password = $.trim($('#password').val());
	            // 密码不为空
	            if(password && password.length >= 6){
	                // 检查密码提示问题答案
	                _user.resetPassword({
	                    username : _this.data.username,
	                    passwordNew : password,
	                    forgetToken : _this.data.token
	                }, function(res){
	                    window.location.href = './result.html?type=pass-reset';
	                }, function(errMsg){
	                    formError.show(errMsg);
	                });
	            }
	            // 密码为空
	            else{
	                formError.show('请输入不少于6位的新密码');
	            }
	        });
	    },
	    // 加载输入用户名的一步
	    loadStepUsername : function(){
	        $('.step-username').show();
	    },
	    // 加载输入密码提示答案的一步
	    loadStepQuestion : function(){
	        // 清除错误提示
	        formError.hide();
	        // 做容器的切换   
	        $('.step-username').hide().
	            siblings('.step-question').show().
	            find('.question').text(this.data.question);
	    },
	    // 加载输入密码的一步
	    loadStepPassword : function(){
	        // 清除错误提示
	        formError.hide();
	        // 做容器的切换   
	        $('.step-question').hide().
	            siblings('.step-password').show();
	    }
	};

	$(function(){
	    page.init();
	});

/***/ }),

/***/ 145:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ })

});