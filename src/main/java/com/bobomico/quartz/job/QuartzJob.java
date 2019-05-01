package com.bobomico.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: com.bobomico.quartz.job.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/4/30  12:39
 * @Description: Quartz任务抽象类
 * @version:
 */
abstract class QuartzJob implements Job {

    private static Logger log = LoggerFactory.getLogger(QuartzJob.class);

    private int CUR_RETRY_COUNT = 0;
    private static int MAX_RETRY_COUNT = 3; // 任务执行失败，最大重试次数

    void retryExecJob(Exception e, JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String jobName = jobExecutionContext.getJobDetail().getKey().getName();
        if (CUR_RETRY_COUNT < MAX_RETRY_COUNT) {
            CUR_RETRY_COUNT++;
            // 通过JobExecutionException异常,通知scheduler如何处理
            JobExecutionException jee = new JobExecutionException(e);
            jee.setRefireImmediately(true); // 立即重新执行任务
            // jee.setUnscheduleAllTriggers(true); // 立即停止所有相关这个任务的触发器
            log.warn("jobName:{} is retry exec. retryCount:{}", jobName, CUR_RETRY_COUNT);
            throw jee;
        } else {
            CUR_RETRY_COUNT = 0;
        }
    }
}
