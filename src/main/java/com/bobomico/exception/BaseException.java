package com.bobomico.exception;

/**
 * @ClassName: com.bobomico.exception.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  2:09
 * @Description: 自定义异常基类
 * @version:
 */
public class BaseException extends Exception {
    public BaseException() {}

    public BaseException(String message) {
        super(message);
    }
}
