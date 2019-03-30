package com.bobomico.quartz.stevexie.scheduler;

import com.bobomico.common.Const;
import com.bobomico.observer.Observer;
import com.bobomico.observer.Subject;
import com.bobomico.observer.UserStatusSubject;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.logging.Logger;

/**
 * @ClassName: org.stevexie.scheduler.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/23  20:35
 * @Description: 用户状态解锁调度器
 * @version:
 */
public class UserScheduler implements Observer {

    private static int count = 0;

    private static final Logger logger = Logger.getLogger("UserScheduler");

    private String observerName;

    private boolean isUnlock;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobDetail jobDetail;

    @Autowired
    private UserStatusSubject userStatusSubject;

    private SimpleTrigger trigger;

    public UserScheduler(UserStatusSubject userStatusSubject) {
        // 设置订阅名
        this.setObserverName("UserSchedulerObserver");
        // 添加订阅
        userStatusSubject.attach(this);
    }

    public void unlockJob(Integer userId){
        System.out.println("调度器启动");
        // 设置启动时间
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() + Const.quartz.UNLOCKTIME);

        // 设置调度策略
        trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("unlock", "UserGroup")
                .usingJobData("userId", userId)
                .startAt(startDate)
                .withSchedule(  // 设置调度频度
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInMilliseconds(1)
                                .withRepeatCount(0))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Subject subjectIsLock) {
        count++;
        System.out.println("调度器调用次数" + count);
        isUnlock = ((UserStatusSubject)subjectIsLock).isUnlock();
        if(isUnlock){
            try {
                // 一定要清除任务 否则下次调用时重复添加会引起错误
                scheduler.deleteJob(jobDetail.getKey());
                scheduler.standby();
                logger.info("调度器是否挂起: " + scheduler.isStarted());
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    public String getObserverName() {
        return observerName;
    }

    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }

}