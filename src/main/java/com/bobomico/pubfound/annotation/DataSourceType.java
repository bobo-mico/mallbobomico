package com.bobomico.pubfound.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: com.bobomico.datasource.annotation.mall-bobomico-B
 * @URL:
 * @Author: DELL
 * @Date: 2019/5/1  7:41
 * @Description: 自定义数据源类型注解，标志当前的dao接口使用的数据源类型
 * @version:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceType {
    String value() default "dataSource";
}