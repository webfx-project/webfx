package webfx.framework.router.session;

import webfx.framework.router.RoutingContext;
import webfx.framework.router.session.impl.SessionHandlerImpl;
import webfx.platforms.core.util.async.Handler;
import webfx.platforms.core.util.function.Callable;
import webfx.platforms.core.util.function.Consumer;

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
