webpackJsonp([6],{

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(141);


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

/***/ 141:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-06 04:57:11
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-04-02 15:24:12
	*/

	/*禁用严格模式*/
	/*'use strict';*/
	__webpack_require__(142);
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
	        // 登陆按钮的点击
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
	            type     : $.trim('email'),
	            username : $.trim($('#username').val()),
	            password : $.trim($('#password').val())
	        },
	        // 表单验证结果
	        validateResult = this.formValidate(formData);
	        // 验证成功
	        if(validateResult.status){
	            _user.login(formData, function(res){    // 经过测试这个res没有用 我觉得这个应该删掉 不然容易误会
	                window.location.href = _mm.getUrlParam('redirect') || './index.html';
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
	        if( !_mm.validate(formData.username, 'require')){
	            result.msg = '用户名不能为空';
	            return result;
	        }
	        if( !_mm.validate(formData.password, 'require')){
	            result.msg = '密码不能为空';
	            return result;
	        }
	        // 通过验证 返回正确提示
	        result.status = true;
	        result.msg = '验证通过';
	        return result;
	    }
	};

	$(function(){
	    page.init();
	});

/***/ }),

/***/ 142:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ })

});