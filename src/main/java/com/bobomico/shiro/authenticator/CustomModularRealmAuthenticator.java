package com.bobomico.shiro.authenticator;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * @ClassName: com.bobomico.shiro.authenticator.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/28  3:37
 * @Description: 重写 认证器
 * @version:
 */
@Component
public class CustomModularRealmAuthenticator  extends ModularRealmAuthenticator {

    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        // TODO Auto-generated method stub
        // 判断realm是否为空
        assertRealmsConfigured();

        // 所有Realm
        Collection<Realm> realms = getRealms();

        // 判断是单Realm还是多Realm
        if (realms.size() == 1)
            return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
        else
            return doMultiRealmAuthentication(realms, authenticationToken);
    }

    /**
     * 自定义抛出异常 - todo
     * @param realms
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    public AuthenticationInfo doMultiRealmAuthentication(
            Collection<Realm> realms, AuthenticationToken token) throws AuthenticationException {
        AuthenticationStrategy strategy = this.getAuthenticationStrategy();
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);

        Iterator var5 = realms.iterator();

        while(var5.hasNext()) {
            Realm realm = (Realm)var5.next();
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
            if (realm.supports(token)) {

                AuthenticationInfo info = null;
                Throwable t = null;

                info = realm.getAuthenticationInfo(token);

                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
            }
        }
        aggregate = strategy.afterAllAttempts(token, aggregate);
        return aggregate;
    }
}
