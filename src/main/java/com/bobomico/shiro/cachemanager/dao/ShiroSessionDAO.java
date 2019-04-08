// package com.bobomico.shiro.cachemanager.dao;
//
// import org.apache.shiro.session.Session;
// import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
// import org.springframework.stereotype.Component;
//
// import java.io.Serializable;
//
// /**
//  * @ClassName: com.bobomico.shiro.cachemanager.dao.mallbobomico
//  * @Author: DELL
//  * @Date: 2019/4/8  8:08
//  * @Description:
//  * @version:
//  */
// @Component
// public class ShiroSessionDAO extends CachingSessionDAO {
//
//     @Override
//     protected void doUpdate(Session session) {
//     }
//
//     @Override
//     protected void doDelete(Session session) {
//     }
//
//     @Override
//     protected Serializable doCreate(Session session) {
//         // 这里绑定sessionId到session，必须要有
//         Serializable sessionId = generateSessionId(session);
//         assignSessionId(session, sessionId);
//         return sessionId;
//     }
//
//     @Override
//     protected Session doReadSession(Serializable sessionId) {
//         return null;
//     }
// }