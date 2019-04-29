package com.bobomico.quartz.stevexie.jobdetail;

import com.bobomico.common.Const;
import com.bobomico.dao.po.SysUserLogin;
import com.bobomico.observer.UserStatusSubject;
import com.bobomico.service.IUserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;

/**
 * @ClassName: org.stevexie.jobdetail.bobomiccore2
 * @Author: Lion
 * @Date: 2019/3/23  19:12
 * @Description: 用户状态解锁任务
 * @version:
 */
public class UnlockJob implements Job {

    private static final Logger logger = Logger.getLogger("UnlockJob");

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private SysUserLogin sysUserLogin;

    @Autowired
    private UserStatusSubject userStatusSubject;

    private Integer userId;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("执行解锁任务");
        // 执行解锁任务
        sysUserLogin.setSysUserId(userId);
        sysUserLogin.setUserStats((byte)Const.userStatus.ACTIVATED);
        int result = userServiceImpl.updateUserStatus(sysUserLogin);
        if(result > 0){
            logger.info("该用户已解锁");
            userStatusSubject.setUnlock(Boolean.TRUE); // 发布更新
        }else{
            logger.info("数据库异常");
        }
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}