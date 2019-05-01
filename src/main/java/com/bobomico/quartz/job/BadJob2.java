package com.bobomico.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

/**
 * @ClassName: com.bobomico.quartz.job.mall-bobomico-B
 * @URL: https://blog.csdn.net/lnara/article/details/8668107
 * @Author: DELL
 * @Date: 2019/5/1  1:27
 * @Description:
 * @version:
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BadJob2 implements Job {

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        // 任务执行的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String jobName = context.getJobDetail().getKey().getName();
        System.out.println("---" + jobName + " 在[ " + dateFormat.format(new Date())+ " ] 执行!!  ") ;
        System.err.println("--- 在 BadJob  2   中发生 错误, 将停止运行!! ");
        JobExecutionException e2 = new JobExecutionException(new Exception());
        // 设置 将自动 去除 这个任务的触发器,所以这个任务不会再执行
        e2.setUnscheduleAllTriggers(true);
        // 抛出异常
        throw e2;
    }
}