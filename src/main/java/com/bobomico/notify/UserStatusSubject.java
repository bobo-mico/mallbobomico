package com.bobomico.notify;

import com.bobomico.notify.subject.Subject;
import org.springframework.stereotype.Component;

/**
 * @ClassName: com.quartz.stevexie.observer
 * @Author: Lion
 * @Date: 2019/3/24  2:42
 * @Description: 主题 - 由Job主动修改并通知给以下订阅者
 *                  数据库中用户账户锁定状态更新后
 *                  1 调度器 删除Job并挂起
 *                  2 凭证匹配器 同步缓存中的账户锁定状态
 * @version:
 */
@Component("userStatusSubject")
public class UserStatusSubject extends Subject {

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
