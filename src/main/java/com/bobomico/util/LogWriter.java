package com.bobomico.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @ClassName: com.bobomico.util.mall-bobomico-B
 * @URL: https://blog.csdn.net/javaacmer/article/details/39180627
 * @Author: DELL
 * @Date: 2019/5/1  5:32
 * @Description:
 * @version:
 */
public class LogWriter {
    // 可以写作配置：true写文件; false输出控制台
    private static boolean fileLog = true;
    private static String logFileName = "/home/yzx/yzx/elong/sysout.log";

    public static void log(String info) throws IOException {
        OutputStream out = getOutputStream();
        out.write(info.getBytes("utf-8"));
    }

    public static OutputStream getOutputStream() throws IOException {
        if (fileLog) {
            File file = new File(logFileName);
            if (!file.exists())
                file.createNewFile();
            return new FileOutputStream(file);
        } else {
            return System.out;
        }
    }
}
