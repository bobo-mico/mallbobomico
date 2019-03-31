package com.bobomico.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @ClassName: com.bobomico.common.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  6:54
 * @Description: 常量类
 * @version:
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String USERID = "userId";
    public static final String PHONE="PHONE";

    // 动态分组
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public interface shiro{
        String HASHALGORITHM = "md5";
        int HASHITERATIONS = 1;
        int RETRYNUMBER = 5;
    }

    public interface userStatus{
        int NOTACTIVE = 0;
        int ACTIVATED = 1;
        int LOCKED = 2;
    }

    public interface quartz{
        long UNLOCKTIME = 3000;
    }

    public interface activation{
        String CHECK_CODE = "checkCode";
        String ACTIVATION_URL = "http://localhost:8091/email/activate?id=";
        String FROM = "3370659455@qq.com";
        String PASSWORD = "vcylzzdrsuctchch";
    }

    // 内部枚举
    public enum ProductStatusEnum{
        ON_SALE(1, "在线");
        private String value;
        private int code;
        ProductStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        public int getCode() {
            return code;
        }
    }

}
