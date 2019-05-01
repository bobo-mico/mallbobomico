package com.bobomico.shiro.cache.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.util.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  18:03
 * @Description: 序列化和反序列化工具
 * @version:
 */
@Slf4j
// @Deprecated
public class SerializeUtils {

    /**
     * 反序列化
     * @param bytes
     * @return
     */
    public static Object deserialize(byte[] bytes) {
        Object result = null;
        if (isEmpty(bytes)) {
            return null;
        }
        ByteArrayInputStream byteStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            byteStream = new ByteArrayInputStream(bytes);
            try {
                objectInputStream = new ObjectInputStream(byteStream);
                try {
                    result = objectInputStream.readObject();
                }
                catch (ClassNotFoundException ex) {
                    throw new Exception("Failed to deserialize object type", ex);
                }
            }
            catch (Throwable ex) {
                throw new Exception("Failed to deserialize", ex);
            }
        } catch (Exception e) {
            log.error("Failed to deserialize",e);
        } finally {
            close(objectInputStream);
            close(byteStream);
        }
        return result;
    }

    public static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    /**
     * 序列化
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        byte[] result = null;
        if (object == null) {
            return new byte[0];
        }
        ByteArrayOutputStream byteStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            byteStream = new ByteArrayOutputStream(128);
            try  {
                if (!(object instanceof Serializable)) {
                    throw new IllegalArgumentException(SerializeUtils.class.getSimpleName() + " requires a Serializable payload " +
                            "but received an object of type [" + object.getClass().getName() + "]");
                }
                objectOutputStream = new ObjectOutputStream(byteStream);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                result =  byteStream.toByteArray();
            }
            catch (Throwable ex) {
                throw new Exception("Failed to serialize", ex);
            }
        } catch (Exception ex) {
            log.error("Failed to serialize",ex);
        } finally {
            close(objectOutputStream);
            close(byteStream);
        }
        return result;
    }

    /**
     * 关闭流
     * @param closeable
     */
    private static void close(Closeable closeable) {
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("close stream error");
            }
        }

    }
}