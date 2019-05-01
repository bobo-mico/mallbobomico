package com.bobomico.quartz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @ClassName: com.bobomico.quartz.service.mall-bobomico-B
 * @URL: https://github.com/huangyueranbbc/QuartzUtils/blob/QuartzUtils_v1.0.0/src/main/java/com/hyr/quartz/service/JobService.java
 * @Author: DELL
 * @Date: 2019/4/30  12:33
 * @Description: 业务计算入口
 * @version:
 */
public enum JobService {
    instances;

    private static Logger log = LoggerFactory.getLogger(JobService.class);

    public void update() {
        int runTime = new Random().nextInt(5);
        log.info("runTime:{}", runTime);
        for (int i = 1; i <= runTime; i++) {
            try {
                log.info("该任务正在执行:{}", i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
