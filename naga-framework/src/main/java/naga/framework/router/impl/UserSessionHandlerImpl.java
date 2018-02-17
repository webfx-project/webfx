package naga.framework.router.impl;

import naga.framework.router.RoutingContext;
import naga.framework.router.session.UserSessionHandler;
import naga.framework.session.Session;
import naga.platform.services.authz.User;

/**
 * @author Bruno Salmon
 */
public class UserSessionHandlerImpl implements UserSessionHandler {

    private static final String SESSION_USER_HOLDER_KEY = "session.userHolder";

    @Override
    public void handle(RoutingContext context) {
        Session session = context.session();
        if (session != null) {
            User user = null;
            UserHolder holder = session.get(SESSION_USER_HOLDER_KEY);
            if (holder != null) {
                RoutingContext previousContext = holder.context;
                if (previousContext != null)
                    user = previousContext.user();
                 else if (holder.user != null) {
                    user = holder.user;
                    //user.setAuthProvider(authProvider);
                    holder.user = null;
                }
                holder.context = context;
            } else
                // only at the time we are writing the header we should store the user to the session
                //context.addHeadersEndHandler(v -> {
                    // during the request the user might have been removed
                    if (context.user() != null)
                        setSessionUserHolder(session, new UserHolder(context));
                //});
            if (user != null)
                context.setUser(user);
        }
        context.next();
    }

    public static void setSessionUserHolder(Session session, UserHolder userHolder) {
        session.put(SESSION_USER_HOLDER_KEY, userHolder);
    }

}
