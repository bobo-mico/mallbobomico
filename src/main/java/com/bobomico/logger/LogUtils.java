package com.bobomico.logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bobomico.logger.annotation.LogField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSON;

/**
 * @ClassName: com.bobomico.logger.mall-bobomico-B
 * @URL: https://blog.csdn.net/Rodge_Rom/article/details/82962678
 * @Author: DELL
 * @Date: 2019/5/1  5:39
 * @Description:
 * @version:
 */
public class LogUtils {

    // 日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtils.class);
    // 操作类型：add
    private static final String ADD_TYPE = "add";
    // 操作类型：update
    private static final String UPDATE_TYPE = "update";

    /**
     * 获取新增操作的日志内容
     *
     * @param obj 页面对象，必传
     * @param objMap 对象日志常量内容，不必传
     * @return String 获取操作后日志
     */
    public static String getAddLogs(Object obj, Map<String, String> objMap) {
        Map<String, String> dtoMap = getValues(obj, obj.getClass().getDeclaredFields(), ADD_TYPE);
        Map<String, String> childDtoMap = getChildValues(obj, obj.getClass().getDeclaredFields(), ADD_TYPE);
        if (!childDtoMap.isEmpty()) {
            dtoMap.putAll(childDtoMap);
        }
        StringBuilder after = new StringBuilder();
        dtoMap.forEach((k, v) -> {
            after.append(k + v + "，");
        });
        LOGGER.info("新增日志内容：" + JSON.toJSONString(dtoMap));
        String afterLog = after.toString();
        if (StringUtils.isNotBlank(afterLog)) {
            return replaceLogs(afterLog.substring(0, afterLog.lastIndexOf("，")), objMap);
        }
        return null;
    }

    /**
     * 获取修改操作的日志内容
     *
     * @param obj1 数据库对象，必传
     * @param obj2 页面对象，必传
     * @param objMap 对象日志常量内容，不必传
     * @return map.get("before")获取操作前日志，map.get("after")获取操作后日志
     */
    public static Map<String, String> getUpdateLogs(Object obj1, Object obj2, Map<String, String> objMap) {
        // 操作前
        Map<String, String> entityMap = getValues(obj1, obj1.getClass().getDeclaredFields(), UPDATE_TYPE);
        Map<String, String> childEntityMap = getChildValues(obj1, obj1.getClass().getDeclaredFields(), UPDATE_TYPE);
        if (!childEntityMap.isEmpty()) {
            entityMap.putAll(childEntityMap);
        }
        LOGGER.info("对比前内容1：" + JSON.toJSONString(entityMap));

        // 操作后
        Map<String, String> dtoMap = getValues(obj2, obj2.getClass().getDeclaredFields(), UPDATE_TYPE);
        Map<String, String> childDtoMap = getChildValues(obj2, obj2.getClass().getDeclaredFields(), UPDATE_TYPE);
        if (!childDtoMap.isEmpty()) {
            dtoMap.putAll(childDtoMap);
        }
        LOGGER.info("对比前内容2：" + JSON.toJSONString(dtoMap));

        StringBuilder before = new StringBuilder();
        StringBuilder after = new StringBuilder();
        entityMap.forEach((k, v) -> {
            if (dtoMap.containsKey(k) && !v.equals(dtoMap.get(k))) {
                before.append(k + v + "，");
                after.append(k + dtoMap.get(k) + "，");
            }
        });
        LOGGER.info("对比后内容1：" + JSON.toJSONString(before));
        LOGGER.info("对比后内容2：" + JSON.toJSONString(after));
        String beforeLog = before.toString();
        String afterLog = after.toString();
        if (StringUtils.isNotBlank(beforeLog)) {
            Map<String, String> map = new HashMap<>(2);
            map.put("before", replaceLogs(beforeLog.substring(0, beforeLog.lastIndexOf("，")), objMap));
            map.put("after", replaceLogs(afterLog.substring(0, afterLog.lastIndexOf("，")), objMap));
            LOGGER.info("最终比对内容为：" + JSON.toJSONString(map));
            return map;
        }
        return null;
    }

    /**
     * 日志常量内容替换
     *
     * @param logs 日志内容
     * @param objMap 日志常量内容
     * @return
     */
    private static String replaceLogs(String logs, Map<String, String> objMap) {
        if (StringUtils.isNotBlank(logs) && objMap != null && !objMap.isEmpty()) {
            String[] contents = logs.split("，");
            for (int i = 0; i < contents.length; i++) {
                for (Map.Entry<String, String> entry : objMap.entrySet()) {
                    if (entry.getKey().equals(contents[i])) {
                        contents[i] = entry.getValue();
                        break;
                    }
                }
            }
            return StringUtils.join(contents, "，");
        }
        return logs;
    }

    /**
     * 获取对象注解内容、属性值
     *
     * @param t 实体对象
     * @param fields 操作注解的对象
     * @param operateType 操作类型：add 新增， update 修改
     * @return
     */
    private static Map<String, String> getValues(Object t, Field[] fields, String operateType) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Field field : fields) {
            LogField annotation = field.getAnnotation(LogField.class);
            if (annotation != null && StringUtils.isNotBlank(annotation.name())) {
                Class<?> type = field.getType();
                String fieldName = field.getName();
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = ReflectionUtils.findMethod(t.getClass(), methodName);
                if (method != null) {
                    Object value = ReflectionUtils.invokeMethod(method, t);
                    if (ADD_TYPE.equals(operateType)) {
                        if (value != null && StringUtils.isNotBlank(value + "")) {
                            map.put(annotation.name(), type == Date.class ? DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:ss") : String.valueOf(value));
                        }
                        continue;
                    }
                    if (value == null || StringUtils.isBlank(value + "") || "null".equals(value)) {
                        map.put(annotation.name(), "空");
                    } else {
                        map.put(annotation.name(), type == Date.class ? DateFormatUtils.format((Date)value, "yyyy-MM-dd HH:mm:ss") : String.valueOf(value));
                    }
                }
            }
        }
        return map;
    }

    /**
     * 查找实体类内的实体对象属性，并处理之
     *
     * @param fields 操作注解的对象
     * @param operateType 操作类型：add 新增， update 修改
     * @return
     */
    private static Map<String, String> getChildValues(Object t, Field[] fields, String operateType) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Field field : fields) {
            LogField annotation = field.getAnnotation(LogField.class);
            if (annotation != null && StringUtils.isNotBlank(annotation.entityName())) {
                String fieldName = field.getName();
                String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method method = ReflectionUtils.findMethod(t.getClass(), methodName);
                if (method != null) {
                    Object value = ReflectionUtils.invokeMethod(method, t);
                    if (value != null) {
                        Map<String, String> mapChild = getValues(value, value.getClass().getDeclaredFields(), operateType);
                        map.putAll(mapChild);
                    }
                }
            }
        }
        return map;
    }
}
