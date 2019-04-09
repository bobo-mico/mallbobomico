package com.bobomico.shiro.cachemanager2.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.bobomico.shiro.cachemanager2.dao.IShiroSessionRepository;
import com.bobomico.shiro.cachemanager2.util.RedisManager;
import com.bobomico.shiro.cachemanager2.util.SerializeUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.session.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  18:01
 * @Description: sessionDAO的实现类 缓存技术使用redis
 * @version:
 */
@NoArgsConstructor
@Slf4j
public class JedisIShiroSessionRepositoryImpl implements IShiroSessionRepository {

    // redis session key 前缀
    private final String REDIS_SHIRO_SESSION = "shiro-session:";

    // Redis实现 可用redisUtil取代
    private RedisManager redisManager;

    /**
     * 新增或修改session
     * @param session
     */
    @Override
    public void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            log.error("session or session ID is null");
        }
        // 序列化sessionID
        byte[] key = getByteKey(session.getId());
        // 序列化session
        byte[] value = SerializeUtils.serialize(session);
        // 因为redis的时限是以秒计算的 而session的超时时间是以毫秒计算的 所以/1000
        Long timeOut = session.getTimeout() / 1000;
        redisManager.set(key, value, timeOut.intValue());
    }

    /**
     * 删除session
     * @param sessionId
     */
    @Override
    public void deleteSession(Serializable sessionId) {
        if (sessionId == null) {
            log.error("session id is null");
            return;
        }
        byte[] key = getByteKey(sessionId);
        redisManager.del(key);
    }

    /**
     * 获取session
     * @param sessionId
     * @return
     */
    @Override
    public Session getSession(Serializable sessionId) {
        if (null == sessionId) {
            log.error("session id is null");
            return null;
        }
        Session session = null;
        byte[] key = getByteKey(sessionId);
        byte[] value = redisManager.get(key);
        if (null == value)
            return null;
        session = (Session) SerializeUtils.deserialize(value);
        return session;
    }

    /**
     * 获取已激活的全部session
     * @return
     */
    @Override
    public Collection<Session> getAllSessions() {
        Set<Session> sessions = new HashSet();
        Set<byte[]> byteKeys = redisManager.keys(this.REDIS_SHIRO_SESSION + "*");
        if (byteKeys != null && byteKeys.size() > 0) {
            for (byte[] bs : byteKeys) {
                Session s = (Session) SerializeUtils.deserialize(redisManager
                        .get(bs));
                sessions.add(s);
            }
        }
        return sessions;
    }

    /**
     * 装配设置到redis的sessionID todo 改名 assembleSessionKey
     * @param sessionId
     * @return 前缀+sessionID
     */
    private byte[] getByteKey(Serializable sessionId) {
        String preKey = this.REDIS_SHIRO_SESSION + sessionId;
        return preKey.getBytes();
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

}