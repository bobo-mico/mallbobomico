package com.bobomico.dao;

import com.bobomico.dao.po.JobAndTrigger;

import java.util.List;

/**
 * @ClassName: com.bobomico.dao.mall-bobomico-B
 * @URL: https://github.com/tjfy1992/SpringBootQuartz/blob/master/demo/src/main/java/com/example/demo/dao/JobAndTriggerMapper.java
 * @Author: DELL
 * @Date: 2019/5/1  5:57
 * @Description: Quartz可视化
 * @version:
 */
public interface JobAndTriggerMapper {

    List<JobAndTrigger> getJobAndTriggerDetails();

    // 检索SimpleTrigger
    List<JobAndTrigger> getJobAndSimpleTriggerDetails();
}