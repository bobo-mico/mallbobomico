package com.bobomico.util;

import org.springframework.stereotype.Component;

/**
 * @ClassName: com.bobomico.util.bobomicomall
 * @Author: Lion
 * @Date: 2019/3/27  8:56
 * @Description: 用户登录次数缓存 线程安全
 * @version:
 */
@Component
public class RetryCountTransit {
    ThreadLocal<Integer> retryCount = new ThreadLocal(){
        protected Integer initialValue(){
            return null;
        }
    };

    public Integer get(){
        return retryCount.get();
    }

    public void set(Integer count){
        this.retryCount.set(count);
    }
}

