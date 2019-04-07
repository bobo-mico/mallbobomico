package com.bobomico.util;

import com.bobomico.dao.po.SysUserLogin;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.text.SimpleDateFormat;

/**
 * @ClassName: com.bobomico.util.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  3:52
 * @Description:
 * @version:
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        // 序列化配置
        // 对象的所有字段全部列入序列化
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        // 忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 所有的日期格式都统一为以下的样式 即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 反序列化配置
        // 忽略在json字符串中存在 而java类中不存在对应属性的情况 防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转字符串
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error", e);
            return null;
        }
    }

    /**
     * 对象转字符串 上线不推荐使用
     * @param obj
     * @param <T>
     * @return 格式化好的string
     */
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }

    /**
     * 字符串转对象
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T)str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    /**
     * 可处理嵌套泛型 TypeReference
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    /**
     * 可处理嵌套泛型
     * @param str
     * @param collectionClass
     * @param elementClasses
     * @param <T>
     * @return
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses){
        JavaType javaType =
                objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        // TestPojo testPojo = new TestPojo();
        // testPojo.setName("Geely");
        // testPojo.setId(666);
        //
        // //{"name":"Geely","id":666}
        // String json = "{\"name\":\"Geely\",\"color\":\"blue\",\"id\":666}";
        // TestPojo testPojoObject = JsonUtil.string2Obj(json,TestPojo.class);
//        String testPojoJson = JsonUtil.obj2String(testPojo);
//        log.info("testPojoJson:{}",testPojoJson);

        log.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");

        // 声明一个对象
        SysUserLogin user = new SysUserLogin();
        user.setSysUserId(1);
        user.setLoginEmail("geely@happymmall.com");
        user.setModifiedTime(new Date());
        // 将对象转换为string
        String userJson = JsonUtil.obj2String(user);
        String userJsonPretty = JsonUtil.obj2StringPretty(user);
        // 打印
        log.info("userJson:{}",userJson);
        log.info("userJsonPretty:{}",userJsonPretty);

        // 将string转换成对象
        SysUserLogin usertext = JsonUtil.string2Obj(userJson, SysUserLogin.class);
        System.out.println(usertext.toString());

        // 声明第二个对象
        SysUserLogin user2 = new SysUserLogin();
        user2.setSysUserId(2);
        user2.setLoginEmail("geelyu2@happymmall.com");
        // 将两个对象加入集合
        List<SysUserLogin> userList = Lists.newArrayList();
        userList.add(user);
        userList.add(user2);
        // 将集合序列化成字符串
        String userListStr = JsonUtil.obj2StringPretty(userList);
        System.out.println(userListStr);

        log.info("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");

        // 泛型集合的反序列化 如果直接指定list.class 泛型将默认为LinkedHashMap 这里使用TypeReference来指定嵌套泛型
        List<SysUserLogin> userListObj1 = JsonUtil.string2Obj(userListStr, new TypeReference<List<SysUserLogin>>(){});
        System.out.println(userListObj1.get(0).toString());
        System.out.println(userListObj1.get(1).toString());
        // 使用其他方法来完成嵌套泛型的反序列化
        List<SysUserLogin> userListObj2 = JsonUtil.string2Obj(userListStr, List.class, SysUserLogin.class);
        System.out.println(userListObj2.get(0).toString());
        System.out.println(userListObj2.get(1).toString());
        System.out.println("end");

    }
}
