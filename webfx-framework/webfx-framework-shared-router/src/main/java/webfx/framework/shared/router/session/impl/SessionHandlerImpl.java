package webfx.framework.shared.router.session.impl;

import webfx.framework.shared.router.RoutingContext;
import webfx.framework.shared.router.session.Session;
import webfx.framework.shared.router.session.SessionHandler;
import webfx.framework.shared.router.session.SessionStore;
import webfx.platform.shared.util.function.Callable;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class SessionHandlerImpl implements SessionHandler {

    private final Callable<SessionStore> sessionStoreGetter;
    private final Callable<String> sessionIdFetcher;
    private final Consumer<String> sessionIdRecorder;

    public SessionHandlerImpl(Callable<SessionStore> sessionStoreGetter, Callable<String> sessionIdFetcher, Consumer<String> sessionIdRecorder) {
        this.sessionStoreGetter = sessionStoreGetter;
        this.sessionIdFetcher = sessionIdFetcher;
        this.sessionIdRecorder = sessionIdRecorder;
    }

    @Override
    public void handle(RoutingContext context) {
        String sessionId = sessionIdFetcher.call();
        if (sessionId == null) {
            createNewSession(context);
            recordSessionIdAndContinueRouting(context);
        } else {
            sessionStoreGetter.call().get(sessionId).setHandler(ar -> {
                if (ar.failed())
                    context.fail(ar.cause());
                else {
                    Session session = ar.result();
                    if (session != null)
                        context.setSession(session);
                    else
                        createNewSession(context);
                    recordSessionIdAndContinueRouting(context);
                }
            });
        }
    }

    private Session createNewSession(RoutingContext context) {
        SessionStore sessionStore = sessionStoreGetter.call();
        Session session = sessionStore.createSession();
        context.setSession(session);
        sessionStore.put(session);
        return session;
    }

    private void recordSessionIdAndContinueRouting(RoutingContext context) {
        if (sessionIdRecorder != null)
            sessionIdRecorder.accept(context.session().id());
        context.next();
    }
}
