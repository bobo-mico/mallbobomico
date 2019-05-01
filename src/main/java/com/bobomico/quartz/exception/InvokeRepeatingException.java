package com.bobomico.quartz.exception;

import com.bobomico.exception.BaseException;

/**
 * @ClassName: com.bobomico.quartz.exception.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  2:13
 * @Description: 重复调用异常
 * @version:
 */
public class InvokeRepeatingException extends BaseException {
    public InvokeRepeatingException() {
    }

    public InvokeRepeatingException(String message) {
        super(message);
    }
}
