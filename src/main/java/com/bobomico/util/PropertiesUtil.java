package com.bobomico.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        String fileName = "bobomico.properties";
        props = new Properties();
        try {
            props.load(
                    new InputStreamReader(
                            PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            // 在日志中打印异常信息
            logger.error("配置文件读取异常", e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim(); // 处理配置文件中的空格
    }

    // 如果key传错了 导致没取到值 依然可以用传进来的默认值
    public static String getProperty(String key, String defaultValue){
        String value = props.getProperty(key.trim());
        // 如果取值为空
        if(StringUtils.isBlank(value)){
            // 设为默认值
            value = defaultValue;
        }
        return value.trim();
    }
}