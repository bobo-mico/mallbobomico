package com.bobomico.quartz.job.impl;

import com.bobomico.common.Const;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.notify.UserStatusSubject;
import com.bobomico.quartz.job.IBaseJob;
import com.bobomico.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * DisallowConcurrentExecution
 * 添加到 Job 类后，Quartz 将不会同时执行多个 Job 实例（什么是 Job 实例可参看上一节）。
 * 注意措辞。我们用上一节的例子来讲解，如果 “UnlockJob” 上添加了这个 Annotation，那么同时只能执行一个“UnlockJob”，
 * 但是却可以同时执行“UnlockJob2”。因此，可以说这个约束是基于 JobDetail 的而不是基于 Job 的。
 *
 * 该注解可以同一个时刻，同一个任务只能执行一次，不能并行执行两个或多个同一任务。但需要注意的是，多个不同的任务是可以同时执行的。
 *
 * Quartz定时任务默认都是并发执行的，不会等待上一次任务执行完毕，只要间隔时间到就会执行, 如果定时任执行太长，会长时间占用资源，
 * 导致其它任务堵塞。
 * 在Spring中这时需要设置concurrent的值为false, 禁止并发执行。 <property name="concurrent" value="true" />
 * 当不使用spring的时候就需要在Job的实现类上加@DisallowConcurrentExecution的注释
 * @DisallowConcurrentExecution 禁止并发执行多个相同定义的JobDetail, 这个注解是加在Job类上的,
 * 但意思并不是不能同时执行多个Job, 而是不能并发执行同一个Job Definition(由JobDetail定义),
 * 但是可以同时执行多个不同的JobDetail, 举例说明,我们有一个Job类
 * 叫做SayHelloJob, 并在这个Job上加了这个注解, 然后在这个Job上定义了很多个JobDetail,
 * 如sayHelloToJoeJobDetail, sayHelloToMikeJobDetail, 那么当scheduler启动时,
 * 不会并发执行多个sayHelloToJoeJobDetail或者sayHelloToMikeJobDetail,
 * 但可以同时执行sayHelloToJoeJobDetail跟sayHelloToMikeJobDetail
 *
 * 总结 上面花了很大篇幅说明了该标记作用域JobDetail的Job Definition
 * 而最重要的部分则没有加以说明，根据我的经验推断，因为执行任务的时间和间隔可能存在一定矛盾，如执行间隔是10s，而执行时间却用了11s
 * 这时候，下一次执行的行为就跟这个注解有关了，如果添加了注解，表示下一次执行要等待1s，也就是进入阻塞状态。如果没添加注解，
 * 则立刻启动一条新的线程开始执行任务
 *
 * @PersistJobDataAfterExecution
 * 添加到 Job 类后，表示 Quartz 将会在成功执行 execute() 方法后（没有抛出异常）更新 JobDetail 的 JobDataMap，
 * 下一次执行相同的任务（JobDetail）将会得到更新后的值，而不是原始的值。就像@DisallowConcurrentExecution 一样，
 * 这个注释基于 JobDetail 而不是 Job 类的实例。
 */

/**
 * @ClassName: org.stevexie.jobdetail.bobomiccore2
 * @URL: 说明来自: https://wwwcomy.iteye.com/blog/1747040
 *                https://blog.csdn.net/wjacketcn/article/details/51133098
 *                注解2 https://www.cnblogs.com/mengrennwpu/p/7141986.html?utm_source=itdadao&utm_medium=referral
 *                注解1 https://blog.csdn.net/fly_captain/article/details/83029440
 * @Author: Lion
 * @Date: 2019/3/23  19:12
 * @Description: 用户状态解锁任务
 *                  普通私有成员变量的操作不会影响到下次执行结果，userId每次执行都是初始值0。
 *                  静态变量肯定会保存，myStaticCount每次递增。
 *                  有状态的Job可以理解为多次Job调用期间可以持有一些状态信息，这些状态信息存储在Job实例的JobDataMap中，
 *                  而默认的无状态job每次调用时Job实例都会创建一个新的JobDataMap。
 *                  换言之 Job的状态都是由Job实例的JobDataMap来维护的。
 *
 *                  如果恢复调度现场，传播行为无法恢复，所以要评估之后手动修复
 * @version:
 */
@Slf4j
// @DisallowConcurrentExecution // 虽然添加在Job上而实际影响的是Job实例 因为我们只执行一次 所以不存在这个问题
// @PersistJobDataAfterExecution // 因为我们的Job不带状态 既Job实例没有使用JobDataMap 所以这个注解定义与否都无所谓
public class UnlockJob implements IBaseJob {

    @Autowired
    private IUserService userServiceImpl;
    @Autowired
    private SysUserLogin sysUserLogin;
    @Autowired
    private UserStatusSubject userStatusSubject;
    private Integer userId;

    /**
     * 解锁任务
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            log.info("执行解锁任务");
            sysUserLogin.setSysUserId(userId);
            sysUserLogin.setUserStats((byte)Const.userStatus.ACTIVATED);
            int result = userServiceImpl.updateUserStatus(sysUserLogin);
            if(result > 0){
                log.info("该用户已解锁");
                userStatusSubject.setUnlock(Boolean.TRUE); // 发布更新
            }else{
                log.info("数据库异常");
            }
        }catch (Exception ex){
            // 将自动移除该任务的触发器 所以这个任务不会再执行
            // new JobExecutionException().setUnscheduleAllTriggers(true);
            // 直接抛出异常调度器会重复执行
            throw new JobExecutionException(ex);
        }
    }

    /**
     * 注入用户id
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}