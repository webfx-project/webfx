package webfx.framework.shared.router.session.impl;

import webfx.framework.shared.router.session.Session;
import webfx.framework.shared.router.session.SessionStore;
import webfx.platform.shared.util.async.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class MemorySessionStore implements SessionStore {

    private final Map<String, Session> sessions = new HashMap<>();

    public static MemorySessionStore create() {
        return new MemorySessionStore();
    }

    @Override
    public Session createSession() {
        return new MemorySession();
    }

    @Override
    public Future<Session> get(String id) {
        return Future.succeededFuture(sessions.get(id));
    }

    @Override
    public Future<Boolean> delete(String id) {
        return Future.succeededFuture(sessions.remove(id) != null);
    }

    @Override
    public Future<Boolean> put(Session session) {
        sessions.put(session.id(), session);
        return Future.succeededFuture(true);
    }

    @Override
    public Future<Boolean> clear() {
        sessions.clear();
        return Future.succeededFuture(true);
    }
}
