package com.bobomico.ehcache.exception;

/**
 * @ClassName: com.bobomico.ehcache.exception.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/11  13:50
 * @Description: 自定义EhCache缓存异常
 * @version:
 */
public class CacheException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException() {
        super();
    }
}
