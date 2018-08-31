package webfx.framework.router.session;

import webfx.framework.router.RoutingContext;
import webfx.framework.router.session.impl.SessionHandlerImpl;
import webfx.util.async.Handler;
import webfx.util.function.Callable;
import webfx.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public interface SessionHandler extends Handler<RoutingContext> {

    static SessionHandler create(Callable<SessionStore> sessionStoreGetter, Callable<String> sessionIdFetcher, Consumer<String> sessionIdRecorder) {
        return new SessionHandlerImpl(sessionStoreGetter, sessionIdFetcher, sessionIdRecorder);
    }

    static SessionHandler create(SessionStore sessionStore, Callable<String> sessionIdFetcher, Consumer<String> sessionIdRecorder) {
        return create(() -> sessionStore, sessionIdFetcher, sessionIdRecorder);
    }
}
