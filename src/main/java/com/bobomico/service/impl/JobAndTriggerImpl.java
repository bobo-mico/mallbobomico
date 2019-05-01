package com.bobomico.service.impl;

import java.util.List;

import com.bobomico.dao.JobAndTriggerMapper;
import com.bobomico.dao.po.JobAndTrigger;
import com.bobomico.service.IJobAndTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * @ClassName: com.bobomico.service.impl.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  6:01
 * @Description: Quartz可视化
 * @version:
 */
@Service
public class JobAndTriggerImpl implements IJobAndTriggerService {

    @Autowired
    private JobAndTriggerMapper jobAndTriggerMapper;

    public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
        PageInfo<JobAndTrigger> page = new PageInfo(list);
        return page;
    }

    @Override
    public PageInfo<JobAndTrigger> getJobAndSimpleTriggerDetails(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndSimpleTriggerDetails();
        PageInfo<JobAndTrigger> page = new PageInfo(list);
        return page;
    }

}