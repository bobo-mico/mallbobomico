package com.bobomico.shiro.cachemanager2.dao;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.session.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  18:00
 * @Description: sessionDAO接口 没有更新操作是因为更新也由save来操作
 * @version:
 */
public interface IShiroSessionRepository {
    // 保存session
    void saveSession(Session session);
    // 删除session
    void deleteSession(Serializable sessionId);
    // 获取session
    Session getSession(Serializable sessionId);
    // 获取一组session
    Collection<Session> getAllSessions();
}