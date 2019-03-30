package com.bobomico.common;

/**
 *
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String USERID = "userId";
    public static final String PHONE="PHONE";

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
}
