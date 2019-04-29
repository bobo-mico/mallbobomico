package com.bobomico.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

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
            log.error("配置文件读取异常", e);
        }
    }

    /**
     * 获取String类型的属性
     * @param key
     * @return
     */
    public static String getStringProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim(); // 处理配置文件中的空格
    }

    /**
     * 获取String类型的属性带默认值
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getStringProperty(String key, String defaultValue){
        String value = props.getProperty(key.trim());
        // 如果取值为空
        if(StringUtils.isBlank(value)){
            // 设为默认值
            value = defaultValue;
        }
        return value.trim();
    }

    /**
     * 获取Integer类型属性
     * @param key
     * @return
     */
    public static Integer getIntegerProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return Integer.valueOf(value.trim()); // 处理配置文件中的空格
    }

    /**
     * 获取Integer类型属性 带默认值
     * @param key
     * @param defaultValue
     * @return
     */
    public static Integer getIntegerProperty(String key, int defaultValue){
        String value = props.getProperty(key.trim());
        // 如果取值为空
        if(StringUtils.isBlank(value)){
            // 返回默认值
            return defaultValue;
        }
        return Integer.valueOf(value.trim());
    }

    /**
     * 获取Boolean类型
     * @param key
     * @return
     */
    public static Boolean getBooleanProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return Boolean.parseBoolean(value.trim());
    }

    /**
     * 获取Boolean类型 带默认值
     * @param key
     * @param defaultValue
     * @return
     */
    public static Boolean getBooleanProperty(String key, boolean defaultValue){
        String value = props.getProperty(key.trim());
        // 如果取值为空
        if(StringUtils.isBlank(value)){
            // 返回默认值
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }
    //
    // public static String getProperty(String key){
    //     String value = props.getProperty(key.trim());
    //     if(StringUtils.isBlank(value)){
    //         return null;
    //     }
    //     return value.trim(); // 处理配置文件中的空格
    // }
    //
    // // 如果key传错了 导致没取到值 依然可以用传进来的默认值
    // public static String getProperty(String key, String defaultValue){
    //     String value = props.getProperty(key.trim());
    //     // 如果取值为空
    //     if(StringUtils.isBlank(value)){
    //         // 设为默认值
    //         value = defaultValue;
    //     }
    //     return value.trim();
    // }
}