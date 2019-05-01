package com.bobomico.notify.observer;

import com.bobomico.notify.subject.Subject;

/**
 * @ClassName: com.quartz.stevexie.observer
 * @Author: Lion
 * @Date: 2019/3/24  0:18
 * @Description: 这是一个观察者接口 定义一个更新的接口给那些在目标发生改变的时候被通知的对象
 * @version:
 */
public interface Observer {
    // 更新接口
    void update(Subject subject);
}
