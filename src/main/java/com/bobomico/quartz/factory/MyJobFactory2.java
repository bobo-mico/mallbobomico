package com.bobomico.quartz.factory;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * @ClassName: com.quartz.stevexie.factory.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/24  5:28
 * @Description:
 * @version:
 */
public class MyJobFactory2 extends AdaptableJobFactory {

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入, 这属于Spring的技术, 不清楚的可以查看Spring的API.
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}