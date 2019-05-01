package com.bobomico.quartz;

import com.bobomico.quartz.job.MyJob ;
import com.bobomico.quartz.listener.DefaultJobListener;
import com.bobomico.quartz.listener.DefaultSchedulerListener;
import com.bobomico.quartz.listener.DefaultTriggerListener;
import com.bobomico.quartz.utils.QuartzUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: com.bobomico.quartz.mall-bobomico-B
 * @URL: https://github.com/huangyueranbbc/QuartzUtils/blob/QuartzUtils_v1.0.0/src/main/java/com/hyr/quartz/QuartzUtilsTest.java#L2
 * @Author: DELL
 * @Date: 2019/4/30  12:40
 * @Description: 测试类
 * @version:
 */
public class QuartzUtilsTest {
    public static void main(String[] args) throws SchedulerException {
        StdSchedulerFactory schedulerFactory1 = QuartzUtils.getStdSchedulerFactory(2, Thread.NORM_PRIORITY, "UPLOAD_JOB1", "UPLOAD_JOB1");
        Scheduler scheduler = schedulerFactory1.getScheduler();

        StdSchedulerFactory schedulerFactory2 = QuartzUtils.getStdSchedulerFactory(2, Thread.NORM_PRIORITY, "UPLOAD_JOB2", "UPLOAD_JOB2");
        Scheduler scheduler2 = schedulerFactory2.getScheduler();

        QuartzUtils.addSchedulerShutdownHook(scheduler);
        QuartzUtils.addSchedulerShutdownHook(scheduler2);

        QuartzUtils.startLogPlugin(scheduler, QuartzUtils.LOG_INFO); // 启动日志插件
        QuartzUtils.startShutDownHookPlugin(scheduler); // 启动ShutDownHook插件


        QuartzUtils.startLogPlugin(scheduler2, QuartzUtils.LOG_DEBUG); // 启动日志插件
        QuartzUtils.startShutDownHookPlugin(scheduler2); // 启动ShutDownHook插件

        // 绑定单个Listener监听器
        QuartzUtils.bindSchedulerListenerManager(scheduler, new DefaultSchedulerListener("DefaultSchedulerListener"), new DefaultJobListener("DefaultJobListener"), new DefaultTriggerListener("DefaultTriggerListener"));
        QuartzUtils.bindSchedulerListenerManager(scheduler2, new DefaultSchedulerListener("DefaultSchedulerListener"), new DefaultJobListener("DefaultJobListener"), new DefaultTriggerListener("DefaultTriggerListener"));

        // 绑定多个Listener监听器
        List<SchedulerListener> schedulerListeners = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            schedulerListeners.add(new DefaultSchedulerListener("SchedulerListener--" + i));
        }

        List<JobListener> jobListeners = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            jobListeners.add(new DefaultJobListener("JobListener--" + i));
        }

        List<TriggerListener> triggerListeners = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            triggerListeners.add(new DefaultTriggerListener("TriggerListener--" + i));
        }
        // QuartzUtils.bindSchedulerListenerManagers(scheduler, schedulerListeners, jobListeners, triggerListeners);

        // 注入属性Map
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("jobDesc", "job desc.");

        QuartzUtils.scheduleWithFixedDelay(scheduler, MyJob.class, 0, 1, TimeUnit.SECONDS, -1, "ProducerJob", "QUARTZ-JOB-GROUP");

        // 注入属性
        //QuartzUtils.scheduleWithFixedDelay(scheduler2, MyJob.class, 0, 2, TimeUnit.SECONDS, -1, "ProducerJobData1", "QUARTZ-JOB-GROUP", dataMap);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //System.exit(0);
    }

}
