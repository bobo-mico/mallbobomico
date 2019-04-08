package com.bobomico.shiro.cachemanager2.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.session.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  18:00
 * @Description:
 * @version:
 */
public interface ShiroSessionRepository {

    void saveSession(Session session);

    void deleteSession(Serializable sessionId);

    Session getSession(Serializable sessionId);

    Collection<Session> getAllSessions();
}