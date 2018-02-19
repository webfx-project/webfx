package naga.framework.router.impl;

import naga.framework.router.RoutingContext;
import naga.framework.router.session.UserSessionHandler;
import naga.framework.session.Session;

/**
 * @author Bruno Salmon
 */
public class UserSessionHandlerImpl implements UserSessionHandler {

    private static final String SESSION_USER_HOLDER_KEY = "session.userHolder";

    @Override
    public void handle(RoutingContext context) {
        Session session = context.session();
        if (session != null) {
            Object userPrincipal = null;
            UserHolder userHolder = session.get(SESSION_USER_HOLDER_KEY);
            if (userHolder != null) {
                RoutingContext previousContext = userHolder.context;
                if (previousContext != null)
                    userPrincipal = previousContext.userPrincipal();
                else if (userHolder.userPrincipal != null) {
                    userPrincipal = userHolder.userPrincipal;
                    //user.setAuthProvider(authProvider);
                    userHolder.userPrincipal = null;
                }
                userHolder.context = context;
            } else
                // only at the time we are writing the header we should store the user to the session
                //context.addHeadersEndHandler(v -> {
                    // during the request the user might have been removed
                    if (context.userPrincipal() != null)
                        setSessionUserHolder(session, new UserHolder(context));
                //});
            if (userPrincipal != null)
                context.setUserPrincipal(userPrincipal);
        }
        context.next();
    }

    public static void setSessionUserHolder(Session session, UserHolder userHolder) {
        session.put(SESSION_USER_HOLDER_KEY, userHolder);
    }

}
