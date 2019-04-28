webpackJsonp([5],{

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

	module.exports = __webpack_require__(137);


/***/ }),

/***/ 96:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-14 13:36:01
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-04-03 04:32:45
	*/
	'use strict';
	__webpack_require__(97);
	var _mm     = __webpack_require__(99);
	var _user   = __webpack_require__(103);
	var _cart   = __webpack_require__(104);

	// 导航
	var nav = {
	    init : function(){
	        this.bindEvent();
	        this.loadUserInfo();
	        this.loadCartCount();
	        return this;
	    },
	    bindEvent : function(){
	        // 登录点击事件
	        $('.js-login').click(function(){
	            _mm.doLogin();
	        });
	        // 注册点击事件
	        $('.js-register').click(function(){
	            window.location.href = './user-register.html';
	        });
	        // 注销点击事件
	        $('.js-logout').click(function(){
	            _user.logout(function(res){
	                window.location.reload();
	            }, function(errMsg){
	                _mm.errorTips(errMsg);
	            });
	        });
	    },
	    // 加载用户信息
	    loadUserInfo : function(){
	        _user.checkLogin(function(res){
	            console.log('==================控制台========================', res);
	            var username = res.data.loginName || res.data.loginEmail || res.data.loginPhone;
	            $('.user.not-login').hide().siblings('.user.login').show()
	                .find('.username').text(username);
	        }, function(errMsg){
	            // do nothing
	            $('.user.login').hide().siblings('.user.not-login').show();
	        });
	    },
	    // 加载购物车数量
	    loadCartCount : function(){
	        _cart.getCartCount(function(res){
	            $('.nav .cart-count').text(res || 0);
	        }, function(errMsg){
	            $('.nav .cart-count').text(0);
	        });
	    }
	};

	module.exports = nav.init();

/***/ }),

/***/ 97:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

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

/***/ 104:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: Rosen
	* @Date:   2017-05-17 18:55:04
	* @Last Modified by:   Rosen
	* @Last Modified time: 2017-06-02 17:51:15
	*/

	'use strict';

	var _mm = __webpack_require__(99);

	var _cart = {
	    // 获取购物车数量
	    getCartCount : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/get_cart_product_count.do'),
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 添加到购物车
	    addToCart : function(productInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/add.do'),
	            data    : productInfo,
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 获取购物车列表
	    getCartList : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/list.do'),
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 选择购物车商品
	    selectProduct : function(productId, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/select.do'),
	            data    : {
	                productId : productId
	            },
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 取消选择购物车商品
	    unselectProduct : function(productId, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/un_select.do'),
	            data    : {
	                productId : productId
	            },
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 选中全部商品
	    selectAllProduct : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/select_all.do'),
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 取消选中全部商品
	    unselectAllProduct : function(resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/un_select_all.do'),
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 更新购物车商品数量
	    updateProduct : function(productInfo, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/update.do'),
	            data    : productInfo,
	            success : resolve,
	            error   : reject
	        });
	    },
	    // 删除指定商品
	    deleteProduct : function(productIds, resolve, reject){
	        _mm.request({
	            url     : _mm.getServerUrl('/cart/delete_product.do'),
	            data    : {
	                productIds : productIds
	            },
	            success : resolve,
	            error   : reject
	        });
	    },
	}
	module.exports = _cart;

/***/ }),

/***/ 105:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-14 14:30:51
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-02-23 15:27:39
	*/
	'use strict';
	__webpack_require__(106);

	var _mm = __webpack_require__(99);
	// 通用页面头部
	var header = {
	    init : function(){
	        this.onLoad();
	        this.bindEvent();
	        return this;
	    },
	    // 数据回显
	    onLoad : function(){
	        var keyword = _mm.getUrlParam('keyword');
	        // keyword存在 则回显输入框
	        if(keyword){
	            $('#search-input').val(keyword);
	        };
	    },
	    // 提交
	    bindEvent : function(){
	        var _this = this;
	        // 点击搜索按钮以后 做搜索提交
	        $('#search-btn').click(function(){
	            _this.searchSubmit();
	        });
	        // 输入回车 也要做搜索提交
	        $('#search-input').keyup(function(e){
	            // 13是回车键的keyCode
	            if(e.keyCode === 13){   
	                _this.searchSubmit();
	            }
	        });
	    },
	    // 搜索提交
	    searchSubmit : function(){
	        var keyword = $.trim($('#search-input').val());
	        // 如果提交的时候有参数 正常跳转到list页
	        if(keyword){
	            window.location.href = './list.html?keyword=' + keyword;
	        }
	        // 如果keyword为空 直接返回首页
	        else{
	            _mm.goHome();
	        }
	    }
	};

	header.init();

/***/ }),

/***/ 106:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }),

/***/ 112:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-16 15:09:13
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-02-22 19:00:54
	*/
	'use strict';
	__webpack_require__(113);

	var _mm = __webpack_require__(99);
	var templateIndex = __webpack_require__(115);
	// 侧边导航
	var navSide = {
	    option : {
	        name : '',
	        navList : [
	            {name : 'user-center', desc : '个人中心', href: './user-center.html'},
	            {name : 'order-list', desc : '我的订单', href: './order-list.html'},
	            {name : 'user-pass-update', desc : '修改密码', href: './user-pass-update.html'},
	            {name : 'about', desc : '关于猫狗日记', href: './about.html'}
	        ]
	    },
	    init : function(option){
	        // 合并选项
	        $.extend(this.option, option);
	        this.renderNav();
	    },
	    // 渲染导航菜单
	    renderNav : function(){
	        // 计算active数据
	        for(let i=0, iLength = this.option.navList.length; i < iLength; i++){
	            if(this.option.navList[i].name === this.option.name){
	                this.option.navList[i].isActive = true;
	            }
	        };
	        // 渲染list数据
	        var navHtml = _mm.renderHtml(templateIndex, {
	            navList : this.option.navList
	        });
	        // 把html放入容器
	        $('.nav-side').html(navHtml);
	    }
	};

	module.exports = navSide;

/***/ }),

/***/ 113:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }),

/***/ 115:
/***/ (function(module, exports) {

	module.exports = "{{#navList}}\r\n{{#isActive}}\r\n<li class=\"nav-item active\">\r\n{{/isActive}}\r\n{{^isActive}}\r\n<li class=\"nav-item\">\r\n{{/isActive}}\r\n    <a class=\"link\" href=\"{{href}}\">{{desc}}</a>\r\n</li>\r\n{{/navList}}";

/***/ }),

/***/ 137:
/***/ (function(module, exports, __webpack_require__) {

	/*
	* @Author: DELL
	* @Date:   2019-02-22 13:22:13
	* @Last Modified by:   DELL
	* @Last Modified time: 2019-02-22 18:22:13
	*/
	'use strict'
	__webpack_require__(138);
	__webpack_require__(96);
	__webpack_require__(105);
	var navSide = __webpack_require__(112);
	var _mm = __webpack_require__(99);
	var _user = __webpack_require__(103);
	var templateIndex = __webpack_require__(140);

	/* page逻辑部分 */
	var page = {
	    init: function(){
	        this.onLoad();
	        this.bindEvent();
	    },
	    onLoad : function(){
	        // 初始化左侧菜单
	        navSide.init({
	            name: 'user-center'
	        });
	        // 加载用户信息
	        this.loadUserInfo();
	    },
	    bindEvent : function(){
	        var _this = this;
	        // 点击提交按钮后的动作
	        // JS渲染的HTML 这里无法事先bindEvent 只能通过事件冒泡来添加
	        $(document).on('click', '.btn-submit', function(){  // on就是事件代理 函数就是回调函数
	            // 数据收集
	            var userInfo = {
	                phone : $.trim($('#phone').val()),
	                email : $.trim($('#email').val()),
	                question : $.trim($('#question').val()),
	                answer : $.trim($('#answer').val()),
	            },
	            validateResult = _this.validateForm(userInfo);
	            if(validateResult.status){
	                // 更改用户信息
	                _user.updateUserInfo(userInfo, function(res, msg){
	                    _mm.successTips(msg);
	                    window.location.href = './user-center.html';
	                }, function(errMsg){
	                    _mm.errorTips(validateResult.msg);
	                });
	            }
	            else{
	                _mm.errorTips(validateResult.msg);
	            }
	        });   
	    },
	    // 加载用户信息
	    loadUserInfo : function(){
	        var userHtml = '';
	        _user.getUserInfo(function(res){
	            userHtml = _mm.renderHtml(templateIndex, res);
	            $('.panel-body').html(userHtml);
	        }, function(errMsg){
	            _mm.errorTips(errMsg);
	        });
	    },
	    // 验证字段信息
	    validateForm : function(formData){
	        var result = {
	            status : false,
	            msg : ''
	        };
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

/***/ 138:
/***/ (function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ }),

/***/ 140:
/***/ (function(module, exports) {

	module.exports = "<div class=\"user-info\">\r\n    <div class=\"form-line\">\r\n        <span class=\"label\">用户名：</span>\r\n        <span class=\"text\">{{username}}</span>\r\n    </div>\r\n    <div class=\"form-line\">\r\n        <span class=\"label\">电 话：</span>\r\n        <input class=\"input\" id=\"phone\" autocomplete=\"off\" value=\"{{phone}}\" />\r\n    </div>\r\n    <div class=\"form-line\">\r\n        <span class=\"label\">邮 箱：</span>\r\n        <input class=\"input\" id=\"email\" autocomplete=\"off\" value=\"{{email}}\" />\r\n    </div>\r\n    <div class=\"form-line\">\r\n        <span class=\"label\">问 题：</span>\r\n        <input class=\"input\" id=\"question\" autocomplete=\"off\" value=\"{{question}}\" />\r\n    </div>\r\n    <div class=\"form-line\">\r\n        <span class=\"label\">答 案：</span>\r\n        <input class=\"input\" id=\"answer\" autocomplete=\"off\" value=\"{{answer}}\" />\r\n    </div>\r\n    <span class=\"btn btn-submit\">确认</span>\r\n</div>";

/***/ })

});