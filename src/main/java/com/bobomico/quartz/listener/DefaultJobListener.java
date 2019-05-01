package com.bobomico.quartz.listener;

import com.bobomico.quartz.utils.MxBeanManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: com.bobomico.quartz.listener.mall-bobomico-B
 * @URL: https://github.com/huangyueranbbc/QuartzUtils/blob/QuartzUtils_v1.0.0/src/main/java/com/hyr/quartz/listener/DefaultJobListener.java
 * @Author: DELL
 * @Date: 2019/4/30  12:35
 * @Description: 任务监听器
 * @version:
 */
public class DefaultJobListener implements JobListener {

    private static Logger log = LoggerFactory.getLogger(DefaultJobListener.class);

    // 监听器名称
    private String name;

    // 线程安全
    private static ConcurrentHashMap<String, Long> jobStatus = new ConcurrentHashMap<>();

    /**
     * job将要被执行时调用这个方法
     * @param jobExecutionContext
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        // FIXME 测试执行次数是否正常 生产环境删除
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        String triggerName = jobExecutionContext.getTrigger().getKey().getName();
        if (!jobStatus.containsKey(jobName)) {
            jobStatus.put(jobName, 0L);
        }
        Long jobCount = jobStatus.get(jobName);
        jobStatus.put(jobName, jobCount + 1); // 执行一次
        log.info(getName() + " - 该任务:{}准备执行. 执行次数:{} ,触发器名称:{}", jobName, jobCount, triggerName);
    }

    /**
     * 即将被执行，但又被 TriggerListener 否决了时调用这个方法。
     * @param jobExecutionContext
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        log.warn(getName() + " - 该任务:{}已被触发器否决.", jobName);
    }
    
    /**
     * job被执行之后调用这个方法
     * @param jobExecutionContext
     * @param e
     */
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        log.info(getName() + " - 该任务:{}已经执行成功.", jobName);
        MxBeanManager.setLog_level(MxBeanManager.LOG_INFO);
        MxBeanManager.loggingMemoryHistoryDebugInfo(); // 打印内存使用信息
        MxBeanManager.loggingThreadHistoryDebugInfo();
    }

    @Override
    public String getName() {
        return this.name;
    }

    // 构造函数
    public DefaultJobListener(String name) {
        this.name = name;
    }
}
