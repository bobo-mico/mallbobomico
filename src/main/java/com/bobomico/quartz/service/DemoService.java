// package com.bobomico.quartz.service;
//
// import com.bobomico.quartz.service.base.BaseService;
//
// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.List;
//
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
//
// /**
//  * @ClassName: com.bobomico.quartz.service.mall-bobomico-B
//  * @URL:
//  * @Author: DELL
//  * @Date: 2019/5/1  5:29
//  * @Description:
//  * @version:
//  */
// public class DemoService extends BaseService {
//     private final Logger logger = LoggerFactory.getLogger(DemoService.class);
//
//     @Autowired
//     private UserDao userDao;
//
//     public void run() {
//         //设置作业编号
//         setJobId(JobInfoConfig.getDemoQuartz1Id());
//         System.out.println("作业编号:" + JobInfoConfig.getDemoQuartz1Name());
//         //设置作业名称
//         setJobName(JobInfoConfig.getDemoQuartz1Name());
//         System.out.println("作业名称为:" + JobInfoConfig.getDemoQuartz1Name());
//         //开始作业
//         startJob();
//         //获取待处理队列
//         List<User> list = getQueueList();
//         if (list.size() == 0) {
//             endJob();
//             return;
//         }
//         //设置待处理的总数
//         setJobTotal(list.size());
//
//         //循环处理内容
//         for (User user : list) {
//             queueToDeal(user);
//         }
//         //结束作业
//         endJob();
//     }
//
//     public List<User> getQueueList() {
//
//         List<User> list = new ArrayList<User>();
//
//         try {
//
//             UserExample userExample = new UserExample();
//             userExample.createCriteria().andBalanceEqualTo(new BigDecimal(0));
//             userExample.setStart(0);
//             userExample.setLength(Integer.valueOf(JobInfoConfig.getDemoQuartz1Limit()));
//             System.out.println("作业上限:" + JobInfoConfig.getDemoQuartz1Limit());
//             list = userDao.selectByExample(userExample);
//
//         } catch (Exception e) {
//             logger.error(jobName, e);
//         }
//
//         return list;
//
//     }
//
//
//     public void queueToDeal(User user) {
//
//         User users = userDao.selectByPrimaryKey(user.getUserId());
//
//         try {
//
//             if (users != null) {
//                 users.setBalance(new BigDecimal(1));
//                 UserExample userExample = new UserExample();
//                 userExample.createCriteria().andUserIdEqualTo(user.getUserId());
//
//                 if (userDao.updateByExample(users, userExample) <= 0) {
//                     setFailJob(users.getUserId().toString());
//                     return;
//                 }
//                 setSuccessJob(users.getUserId().toString());
//
//             } else {
//
//                 setFailJob(user.getUserId().toString());
//                 return;
//             }
//
//         } catch (Exception e) {
//             setFailJob(users.getUserId().toString());
//             logger.error(e.toString());
//         }
//
//     }
// }
