package com.bobomico.util;

import java.math.BigDecimal;

/**
 * @ClassName: com.bobomico.util.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/1  7:17
 * @Description: 在使用BigDecimal的时候一定要选择它的字符串构造器
 *                  ctrl + o
 * @version:
 */
public class BigDecimalUtil {

    public static BigDecimal add(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }


    public static BigDecimal mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        // 调用除法函数时可能有除不尽情况 这时候通过另外一个构造器 说明除法策略
        return b1.divide(b2,2, BigDecimal. ROUND_HALF_UP); // 四舍五入 保留2位小数
    }

    private BigDecimalUtil(){}
}
