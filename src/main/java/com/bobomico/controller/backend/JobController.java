package com.bobomico.controller.backend;

import java.util.HashMap;
import java.util.Map;

import com.bobomico.dao.po.JobAndTrigger;
import com.bobomico.quartz.job.IBaseJob;
import com.bobomico.service.IJobAndTriggerService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;

/**
 * @ClassName: com.bobomico.controller.backend.mall-bobomico-B
 * @URL: https://blog.csdn.net/u012907049/article/details/73801122
 * @Author: DELL
 * @Date: 2019/5/1  5:47
 * @Description: Quartz调度任务管理器
 * @version:
 */
@RestController
@RequestMapping(value="/manage/scheduler/")
public class JobController {

    @Autowired
    private IJobAndTriggerService iJobAndTriggerService;

    //加入Qulifier注解，通过名称注入bean
    @Autowired
    @Qualifier("micoScheduler")
    private Scheduler scheduler;

    private static Logger log = LoggerFactory.getLogger(JobController.class);

    /**
     * 追加任务
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    @PostMapping("add_job.do")
    public void addJob(
            @RequestParam(value="jobClassName")String jobClassName,
            @RequestParam(value="jobGroupName")String jobGroupName,
            @RequestParam(value="cronExpression")String cronExpression) throws Exception{
        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder
                .newJob(getClass(jobClassName).getClass())
                .withIdentity(jobClassName, jobGroupName)
                .build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败" + e);
            throw new Exception("创建定时任务失败");
        }
    }

    /**
     * 暂停任务
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    @PostMapping("pause_job.do")
    public void pauseJob(
            @RequestParam(value="jobClassName")String jobClassName,
            @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    /**
     * 继续任务
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    @PostMapping("resume_job.do")
    public void resumeJob(
            @RequestParam(value="jobClassName")String jobClassName,
            @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    /**
     * 重置任务
     * @param jobClassName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    @PostMapping("reschedule_job.do")
    public void rescheduleJob(
            @RequestParam(value="jobClassName")String jobClassName,
            @RequestParam(value="jobGroupName")String jobGroupName,
            @RequestParam(value="cronExpression")String cronExpression) throws Exception {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败"+e);
            throw new Exception("更新定时任务失败");
        }
    }

    /**
     * 删除任务
     * @param jobClassName
     * @param jobGroupName
     * @throws Exception
     */
    @PostMapping("delete_job.do")
    public void deleteJob(
            @RequestParam(value="jobClassName")String jobClassName,
            @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    /**
     * 查询任务
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("query_job.do")
    public Map<String, Object> queryJob(
            @RequestParam(value="pageNum")Integer pageNum,
            @RequestParam(value="pageSize")Integer pageSize) {
        PageInfo<JobAndTrigger> jobAndTrigger = iJobAndTriggerService.getJobAndSimpleTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap();
        map.put("JobAndTrigger", jobAndTrigger);
        map.put("number", jobAndTrigger.getTotal());
        return map;
    }

    /**
     * 获取任务
     * @param classname
     * @return
     * @throws Exception
     */
    public static IBaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (IBaseJob)class1.newInstance();
    }
}
