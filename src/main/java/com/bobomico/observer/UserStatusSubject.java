package com.bobomico.observer;

import org.springframework.stereotype.Component;

/**
 * @ClassName: com.quartz.stevexie.observer
 * @Author: Lion
 * @Date: 2019/3/24  2:42
 * @Description:
 * @version:
 */
@Component("userStatusSubject")
public class UserStatusSubject extends Subject{

    // 发布内容 用户锁定状态
    private boolean isUnlock;

    public boolean isUnlock() {
        return isUnlock;
    }

    public void setUnlock(boolean unlock) {
        this.isUnlock = unlock;
        // 传播
        this.notifyObservers();
    }
}
