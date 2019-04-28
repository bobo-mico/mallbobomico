package com.bobomico.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: com.bobomico.util.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  0:17
 * @Description: FTP上传工具
 * @version:
 */
public class FTPUtil {

    private static  final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    // ip 账户 密码
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;    // apache commons client包下的工具

    /**
     * 对外上传接口
     * @param fileList
     * @return 是否上传成功
     * @throws IOException
     */
    public static boolean uploadFile(List<File> fileList) throws IOException {
        // 设置ip 端口号 账户 密码
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 14147, ftpUser, ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("/img", fileList);
        logger.info("开始连接ftp服务器,结束上传,上传结果:{}", result);
        return result;
    }

    /**
     * 上传
     * @param remotePath
     * @param fileList
     * @return
     * @throws IOException
     */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        // 连接FTP服务器
        if(connectServer(this.ip, this.port, this.user, this.pwd)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);       // 工作目录
                ftpClient.setBufferSize(1024);                      // 设置缓冲区
                ftpClient.setControlEncoding("UTF-8");              // 设置编码
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);  // 二进制文件类型 防止乱码
                // ftpClient.enterLocalPassiveMode();                  // 打开本地被动模式
                // 循环上传 IO流
                for(File fileItem : fileList){
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;       // 异常时返回给调用者
                e.printStackTrace();
            } finally {
                fis.close();            // 关闭流
                ftpClient.disconnect(); // 释放连接
            }
        }
        return uploaded;
    }

    /**
     * 连接服务器
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     */
    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public FTPUtil(String ip,int port,String user,String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }
}
