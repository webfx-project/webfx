package naga.framework.router.session.impl;

import naga.framework.router.RoutingContext;
import naga.framework.router.session.SessionHandler;
import naga.framework.router.session.Session;
import naga.framework.router.session.SessionStore;

/**
 * @author Bruno Salmon
 */
public abstract class SessionHandlerImplBase implements SessionHandler {

    private final SessionStore sessionStore;

    public SessionHandlerImplBase(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @Override
    public void handle(RoutingContext context) {
        handle(context, fetchSessionId(), sessionStore);
    }

    protected abstract String fetchSessionId();

    public static String handle(RoutingContext context, String sessionId, SessionStore sessionStore) {
        if (sessionId == null) {
            sessionId = createNewSession(context, sessionStore).id();
            context.next();
        } else {
            sessionStore.get(sessionId).setHandler(ar -> {
                if (ar.failed())
                    context.fail(ar.cause());
                else {
                    Session session = ar.result();
                    if (session != null)
                        context.setSession(session);
                    else
                        createNewSession(context, sessionStore);
                    context.next();
                }
            });
        }
        return sessionId;
    }

    private static Session createNewSession(RoutingContext context, SessionStore sessionStore) {
        Session session = sessionStore.createSession();
        context.setSession(session);
        sessionStore.put(session);
        return session;
    }
}
