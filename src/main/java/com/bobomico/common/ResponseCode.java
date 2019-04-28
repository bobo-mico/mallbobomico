package com.bobomico.common;

/**
 * @ClassName: com.bobomico.common.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  6:54
 * @Description: 枚举 - 只能在提供的对象中选择一个
 * @version:
 */
public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),                   // 成功
    ERROR(1,"ERROR"),                       // 错误
    NEED_LOGIN(10,"NEED_LOGIN"),            // 需登录
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"); // 非法参数

    private final int code;
    private final String desc;

    // 枚举的构造函数都是私有构造函数 枚举是限制生产对像的 不是外部调用的 是枚举内部调用的
    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 因为它的构造函数是私有的 对像也是无法访问的 所以需要提供两个getter函数 供外部获取对像
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
