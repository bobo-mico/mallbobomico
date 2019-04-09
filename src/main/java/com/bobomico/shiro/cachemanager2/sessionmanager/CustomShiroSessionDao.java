package com.bobomico.shiro.cachemanager2.sessionmanager;

import java.io.Serializable;
import java.util.Collection;

import com.bobomico.shiro.cachemanager2.dao.IShiroSessionRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

/**
 * @ClassName: com.bobomico.shiro.cachemanager2.session.mallbobomico
 * @Author: DELL
 * @Date: 2019/4/8  17:58
 * @Description: session dao 被注入到安全管理器
 *                  可对session执行增删改查操作
 * @version:
 */
@Getter
@Setter
@Slf4j
public class CustomShiroSessionDao extends AbstractSessionDAO {

    // Session Repository - session仓库 对session执行CRUD操作
    private IShiroSessionRepository sessionDAO;

    /**
     * 创建session
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        // 通过ID生成器生成sessionID
        Serializable sessionId = this.generateSessionId(session);
        // 将sessionID装配到session
        this.assignSessionId(session, sessionId);
        // 缓存session到Redis
        getSessionDAO().saveSession(session);
        return sessionId;
    }

    /**
     * 删除session
     * @param session
     */
    @Override
    public void delete(Session session) {
        if (session == null) {
            log.error("session or session id is null");
            return;
        }
        Serializable id = session.getId();
        if (id != null)
            getSessionDAO().deleteSession(id);
    }

    /**
     * 更新session
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        getSessionDAO().saveSession(session);
    }

    /**
     * 获取session
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        return getSessionDAO().getSession(sessionId);
    }

    /**
     * 获取所有激活sessions
     * @return 一组session
     */
    @Override
    public Collection<Session> getActiveSessions() {
        return getSessionDAO().getAllSessions();
    }

    /**
     * 更新sessionID 防止会话固定攻击
     * @param session
     * @return
     */
    public Serializable updateSessionId(Session session) {
        // 通过ID生成器生成sessionID
        Serializable sessionId = this.generateSessionId(session);
        // 将sessionID装配到session
        this.assignSessionId(session, sessionId);
        // 缓存session到Redis
        getSessionDAO().saveSession(session);
        return sessionId;
    }
}