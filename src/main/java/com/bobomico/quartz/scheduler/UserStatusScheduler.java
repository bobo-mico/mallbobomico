package com.bobomico.quartz.scheduler;

import com.bobomico.common.Const;
import com.bobomico.notify.observer.Observer;
import com.bobomico.notify.subject.Subject;
import com.bobomico.notify.UserStatusSubject;
import com.bobomico.quartz.exception.InvokeRepeatingException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

/**
 * @ClassName: org.scheduler.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/23  20:35
 * @Description: 用户状态解锁调度器
 *                  迁移到SchedulerUtil
 * @version:
 */
@Slf4j
@Deprecated
public class UserStatusScheduler implements Observer {

    // FIXME 测试属性 上线删除
    private static int count = 0;
    private String observerName;
    @Autowired
    @Qualifier("micoScheduler")
    private Scheduler scheduler;
    @Autowired
    private JobDetail jobDetail;

    /**
     * 解锁任务调度器
     * @param userId
     */
    public void unlockJob(Integer userId) throws InvokeRepeatingException {
        // 安全检测
        try {
            if(scheduler.checkExists(jobDetail.getKey())){
                throw new InvokeRepeatingException("警告:该任务已经存在");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        System.out.println("--------------- 调度器初始化 -------------------");

        Date startTime = new Date();
        startTime.setTime(startTime.getTime() + Const.quartz.UNLOCKTIME);

        // 设置解锁时间(延迟启动以这种方法设置的数值只能在0和59之间
        // Date startTime = DateBuilder.nextGivenSecondDate(null, Const.quartz.UNLOCKTIME);

        // 设置调度策略
        SimpleTrigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("unlock", "UserGroup")
                .usingJobData("userId", userId)
                .startAt(startTime)
                .withSchedule(  // 设置调度频度
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInMilliseconds(1)
                                .withRepeatCount(0))
                .build();
        try {
            Date now = scheduler.scheduleJob(jobDetail, trigger);
            log.info("调度器创建时间{}", now);
            scheduler.start();
            log.info("调度器启动");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * FIXME 废弃的方法 已经不需要了 更改了持久性属性 唯一的价值是通知挂起
     * 订阅更新 拉模式
     * @param subjectIsLock
     */
    @Override
    public void update(Subject subjectIsLock) {
        // FIXME 测试数据 上线删除
        log.info("调度器调用次数{}", ++count);

        if(((UserStatusSubject)subjectIsLock).isUnlock()){
            try {
                // 一定要清除任务 否则下次调用时重复添加会引起错误
                scheduler.deleteJob(jobDetail.getKey());
                // 挂起 节省资源
                scheduler.standby();
                log.info("调度器已挂起: " + scheduler.isStarted());
                // 打印已经执行的任务信息
                SchedulerMetaData metaData = scheduler.getMetaData();
                System.out.println("--------  执行了" + metaData.getNumberOfJobsExecuted() + "个 jobs.");
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构造函数
     * 在构造函数中订阅任务执行之后的通知
     * @param userStatusSubject
     */
    public UserStatusScheduler(UserStatusSubject userStatusSubject) {
        // 设置订阅名
        this.setObserverName("账户解锁调度器");
        // 添加订阅
        // userStatusSubject.attach(this);
    }

    /**
     * 获取订阅者名称
     * @return
     */
    public String getObserverName() {
        return this.observerName;
    }

    /**
     * 设置订阅者名称
     * @param observerName
     */
    public void setObserverName(String observerName) {
        this.observerName = observerName;
    }
}

// EOF