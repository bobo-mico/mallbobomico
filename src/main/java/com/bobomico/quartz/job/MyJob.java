package com.bobomico.quartz.job;

import com.bobomico.quartz.service.JobService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @ClassName: com.bobomico.quartz.job.mall-bobomico-B
 * @URL: 说明来自: https://blog.csdn.net/wjacketcn/article/details/51133098
 * @Author: DELL
 * @Date: 2019/4/30  12:38
 * @Description: 注意，我们给了调度器一个 JobDetail 实例，JobDetail 中提供了 Job 的 class 对象，
 *               因此它知道调用的 Job 类型。每次调度器执行 Job，它会在调用 execute(…) 方法前创建一个新的 Job 实例。
 *               当执行完成后，所有 Job 的引用将会丢弃，这些对象会被垃圾回收。
 *               基于前面的描述，首先 Job 类需要一个无参构造方法，另外，在 Job 中存储状态属性是没有意义的，
 *               因为每次执行完成后，对象将会被删除。
 * @version:
 */
@DisallowConcurrentExecution // 不允许并发执行多个Job实例。 当前Job执行完毕后，才会执行下一个。一进一出。
@PersistJobDataAfterExecution // 每次执行JOB后，更新Job内容 保存在JobDataMap传递的参数
public class MyJob extends QuartzJob {

    // private static Logger _log = Logger.getLogger(MyJob.class);
    private static Logger _log = LoggerFactory.getLogger(MyJob.class);

    // 属性注入
    private String jobDesc;

    /**
     * execute方法中仅允许抛出一种类型的异常（包括RuntimeExceptions），即JobExecutionException。你的job可以使用该异常告诉scheduler，你希望如何来处理发生的异常。
     * 如果job发错错误,quartz提供两种方式解决
     * 1 立即重新执行任务
     * 2 立即停止所有相关这个任务的触发器
     * @param jobExecutionContext
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        try {
            _log.info(jobKey + "任务执行前准备" + jobDesc + "  " + new Date());
            long start = System.currentTimeMillis();
            JobService.instances.update();
            long end = System.currentTimeMillis();
            _log.info("任务消费时间" + (end - start));
            // Thread.sleep(10000); // 睡眠10秒，测试DisallowConcurrentExecution
            //throw new Exception(); // 抛出测试异常
        } catch (Exception e) {
            _log.error(jobKey + " execute has error.", e);
            retryExecJob(e, jobExecutionContext);
        }
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }
}