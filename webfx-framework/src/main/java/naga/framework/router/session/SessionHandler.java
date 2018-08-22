package naga.framework.router.session;

import naga.framework.router.RoutingContext;
import naga.framework.router.session.impl.SessionHandlerImpl;
import naga.util.async.Handler;
import naga.util.function.Callable;
import naga.util.function.Consumer;

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
