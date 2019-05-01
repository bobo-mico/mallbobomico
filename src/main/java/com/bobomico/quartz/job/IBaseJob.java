package com.bobomico.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName: com.bobomico.quartz.job.base.mall-bobomico-B
 * @URL: https://blog.csdn.net/weixin_39477597/article/details/81537384
 * @Author: DELL
 * @Date: 2019/5/1  5:19
 * @Description:
 * @version:
 */
public interface IBaseJob extends Job {
    void execute(JobExecutionContext context) throws JobExecutionException;
}
