package com.bobomico.exception;

/**
 * @ClassName: com.bobomico.exception.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  2:12
 * @Description:
 * @version:
 */
public class PayException extends BaseException {
    public PayException() {
        super("支付异常");
    }

    public PayException(String message) {
        super(message);
    }
}
