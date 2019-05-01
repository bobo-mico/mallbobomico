package com.bobomico.exception;

/**
 * @ClassName: com.bobomico.exception.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  2:10
 * @Description: 自定义账单异常
 * @version:
 */
public class BillException extends BaseException{
    public BillException() {
    }

    public BillException(String message) {
        super(message);
    }

    public String customException() {
        return "账单异常";
    }
}
