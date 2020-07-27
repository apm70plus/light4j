package com.light.auth.config.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;

import java.io.Serializable;

public class ShiroSessionDAO extends CachingSessionDAO {
    @Override
    protected void doUpdate(Session session) {
        // do nothing
    }

    @Override
    protected void doDelete(Session session) {
        // do nothing
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        // do nothing
        return null;
    }
}
