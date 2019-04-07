package com.bobomico.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @ClassName: com.bobomico.common.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  6:54
 * @Description: 常量类
 * @version:
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String USERID = "userId";
    public static final String PHONE="phone";

    // 动态分组
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    // 购物车
    public interface Cart{
        int CHECKED = 1;//即购物车选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    // shiro加密相关
    public interface shiro{
        String HASHALGORITHM = "md5";
        int HASHITERATIONS = 1;
        int RETRYNUMBER = 5;
    }

    // 用户账号状态
    public interface userStatus{
        int NOTACTIVE = 0;
        int ACTIVATED = 1;
        int LOCKED = 2;
    }

    // 自动解锁时间
    public interface quartz{
        long UNLOCKTIME = 3000;
    }

    // 通过邮件激活账户
    public interface activation{
        String CHECK_CODE = "checkCode";
        String ACTIVATION_URL = "http://localhost:8091/email/activate?id=";
        String FROM = "3370659455@qq.com";
        String PASSWORD = "vcylzzdrsuctchch";
    }

    // 商品状态
    public enum ProductStatusEnum{
        ON_SALE(1, "在售");
        private String value;
        private int code;
        ProductStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
    }

    // 订单状态
    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"待支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");

        OrderStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    // 支付宝回调(在支付宝文档中查阅
    public interface  AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    // 第三方支付选项
    public enum PayPlatformEnum{
        ALIPAY(1,"支付宝");

        PayPlatformEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    // 支付方式
    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        PaymentTypeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }

        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }
}
