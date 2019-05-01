package com.bobomico.quartz.job.test.impl;

import java.util.Date;

import com.bobomico.quartz.job.IBaseJob;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName: com.bobomico.quartz.job.test.impl.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  6:19
 * @Description: Quartz可视化测试任务
 * @version:
 */
@NoArgsConstructor
public class NewJob implements IBaseJob {

    private static Logger _log = LoggerFactory.getLogger(NewJob.class);

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        _log.error("New Job执行时间: " + new Date());

    }
}