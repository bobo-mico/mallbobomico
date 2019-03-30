package com.bobomico.common;

/**
 * 枚举 - 只能在提供的对象中选择一个
 * 响应状态码
 */
public enum ResponseCode {
    SUCCESS(1, "操作成功"), // 对像
    ERROR(0, "操作失败");   // 对像

    private final int code;
    private final String desc;

    // 枚举的构造函数都是私有构造函数 枚举是限制生产对像的 不是外部调用的 是枚举内部调用的
    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 因为它的构造函数是私有的 对像也是无法访问的 所以需要提供两个getter函数 供外部获取对像
    // alt + insert + fn
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
