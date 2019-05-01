package com.bobomico.service;

import com.bobomico.dao.po.JobAndTrigger;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: com.bobomico.service.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  6:00
 * @Description: Quartz可视化
 * @version:
 */
public interface IJobAndTriggerService {
    PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
    PageInfo<JobAndTrigger> getJobAndSimpleTriggerDetails(int pageNum, int pageSize);
}