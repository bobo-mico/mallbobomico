// package com.bobomico.util;
//
// import com.timisakura.VO.CustomerLoginVO;
// import com.timisakura.pojo.UserLogin;
// import org.springframework.stereotype.Component;
// import javax.mail.*;
// import javax.mail.internet.InternetAddress;
// import javax.mail.internet.MimeMessage;
// import java.util.Date;
// import java.util.Properties;
//
// /**
//  * @ClassName: com.tsmall.util.timisakuramall
//  * @Author: Lion
//  * @Date: 2019/2/24  20:00
//  * @Description:
//  * @version:
//  */
// @Component("emailUtils")
// public class EmailUtils {
//     private static final String FROM = "3370659455@qq.com"; // 类变量
//
//     //
//     public static void sendAccountActivateEmail(UserLogin userLogin) {
//         Session session = getSession();
//         MimeMessage message = new MimeMessage(session); // 邮箱代理对象
//
//         try {
//             message.setSubject("这是一封激活账号的邮件,自动发送请勿回复.");  // 设置主题
//             message.setSentDate(new Date());    // 设置发送时间
//
//             //setFrom 表示用哪个邮箱发送邮件
//             message.setFrom(new InternetAddress(FROM)); // 网络地址(邮箱)
//             /**
//              * RecipientType.TO||BCC||CC
//              *     TO表示主要接收人
//              *     BCC表示秘密抄送人
//              *     CC表示抄送人
//              */
//             message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(userLogin.getUserEmail()));   // 键值对的方式设置接收人地址(邮箱)
// 			/*message.setContent("<a target='_BLANK' href='"+GenerateLinkUtils.generateActivateLink(user)+"'>"+user.getUsername()+"先生/女士您好，请点击此链接激活账号"+GenerateLinkUtils.generateActivateLink(user)
// 			+"</a>","text/html;charset=utf-8");*/
//             message.setContent(userLogin.getLoginName()+"先生/女士您好，请点击此链接激活账号" + "<a target='_BLANK' href='http://www.baidu.com'>" + GenerateLinkUtils.generateActivateLink(userLogin)
//                     +"</a>","text/html;charset=utf-8");
//             Transport.send(message);    // Transport 发送邮件
//
//         } catch (MessagingException e) {
//             e.printStackTrace();
//         }
//     }
//
//     public static Session getSession() {
//         Properties props = new Properties();    // 创建属性文件 属性文件中的属性 是以键值对的形式存在的
//         props.setProperty("mail.transport.protocol", "smtp");   //指定发送的邮箱的邮箱协议
//         props.setProperty("mail.smtp.host","smtp.qq.com");      //指定SMTP服务器
//         props.setProperty("mail.smtp.port", "25");              //smtp是发信邮件服务器, 端口是25
//         props.setProperty("mail.smtp.auth","true");             //指定是否需要SMTP验证
//
//         // 会话 属性文件初始化session对象
//         Session session = Session.getInstance(props, new Authenticator() {
//             @Override
//             protected PasswordAuthentication getPasswordAuthentication() {
//                 return new PasswordAuthentication(FROM, "vcylzzdrsuctchch");
//             }
//         });
//         return session; // 包含了邮箱代理协议 初始化的所有信息
//     }
// }
