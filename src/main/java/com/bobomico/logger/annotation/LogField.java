package com.bobomico.logger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: com.bobomico.logger.annotation.mall-bobomico-B
 * @URL: https://blog.csdn.net/Rodge_Rom/article/details/82962678
 * @Author: DELL
 * @Date: 2019/5/1  5:36
 * @Description:
 * @version:
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LogField {

    /**
     * 列名
     */
    String name() default "";

    /**
     * 实体属性列名
     */
    String entityName() default "";

}
