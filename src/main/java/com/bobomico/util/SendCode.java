// package com.bobomico.util;
//
// import sun.net.www.http.HttpClient;
//
// /**
//  * @ClassName: com.bobomico.util.mallbobomico
//  * @Author: DELL
//  * @Date: 2019/4/2  13:29
//  * @Description:
//  * @version:
//  */
// public class SendCode {
//     public static String sendCode(String url,String encoded,String mobile,String SMSTemplate){
//         //获取随机6位验证码
//         String code = VerifyCodeUtils.generateVerifyCode(6);
//         HttpClient client = new HttpClient();
//         PostMethod post = new PostMethod(url);
//         post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset="+encoded);
//         NameValuePair[] data = {
//                 new NameValuePair("Uid", 用户名),
//                 new NameValuePair("Key", 秘匙),
//                 new NameValuePair("smsMob", mobile),
//                 new NameValuePair("smsText", "验证码："+code+SMSTemplate)};
//         post.setRequestBody(data);
//         try {
//             client.executeMethod(post);
//             Header[] headers = post.getResponseHeaders();
//             int statusCode = post.getStatusCode();
//             System.out.println("statusCode:" + statusCode);
//             for (Header h : headers) {
//                 System.out.println(h.toString());
//             }
//             String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
//             System.out.println(result); // 打印返回消息状态
//
//             post.releaseConnection();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//
//         return code;
//     }
// }
