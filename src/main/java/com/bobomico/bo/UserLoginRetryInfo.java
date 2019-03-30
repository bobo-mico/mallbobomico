package com.bobomico.bo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: com.timisakura.VO.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/24  9:13
 * @Description: 记录用户反复登录信息
 * @version:
 */
public class UserLoginRetryInfo {
    private AtomicInteger atomicInteger;
    private long loginTime;

    public AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    private void setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    public long getLoginTime() {
        return loginTime;
    }

    private void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public UserLoginRetryInfo(AtomicInteger atomicInteger, long loginTime) {
        this.atomicInteger = atomicInteger;
        this.loginTime = loginTime;
    }

    public UserLoginRetryInfo() {
    }
}
