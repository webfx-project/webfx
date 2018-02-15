package naga.framework.router.impl;

import naga.framework.router.auth.authn.RedirectAuthHandler;
import naga.framework.router.auth.authz.RouteAuthority;
import naga.util.Booleans;
import naga.framework.router.RoutingContext;
import naga.framework.session.Session;
import naga.platform.services.auth.spi.authz.User;
import naga.platform.services.auth.spi.authn.AuthService;

/**
 * @author Bruno Salmon
 */
public class RedirectAuthHandlerImpl extends AuthHandlerImpl implements RedirectAuthHandler {

    private final AuthService authService;
    private final String loginPath;
    private final String unauthorizedPath;
    private boolean redirecting;

    public RedirectAuthHandlerImpl(AuthService authService, String loginPath, String unauthorizedPath) {
        this.authService = authService;
        this.loginPath = loginPath;
        this.unauthorizedPath = unauthorizedPath;
    }

    public AuthService getAuthService() {
        return authService;
    }

    @Override
    public void handle(RoutingContext context) {
        if (redirecting)
            context.next();
        else {
            Session session = context.session();
            if (session == null)
                context.fail(new NullPointerException("No session - did you forget to include a SessionHandler?"));
            else {
                User user = context.user();
                if (user == null)
                    redirect(context, loginPath);
                else
                    // Already logged in, just checking the user is authorised to access this route
                    user.isAuthorized(new RouteAuthority(context.path())).setHandler(ar -> {
                        if (ar.succeeded() && Booleans.isTrue(ar.result()))
                            context.next();
                        else
                            redirect(context, unauthorizedPath);
                    });
            }
        }
    }

    private void redirect(RoutingContext context, String redirectPath) {
        try {
            redirecting = true;
            redirectContext(context, redirectPath).next();
        } finally {
            redirecting = false;
        }
    }

    private static RoutingContext redirectContext(RoutingContext context, String redirectPath) {
        if (context instanceof RoutingContextImpl) {
            RoutingContextImpl ctx = (RoutingContextImpl) context;
            return new RoutingContextImpl(ctx.mountPoint(), ctx.router(), redirectPath, ctx.routes, ctx.getParams());
        }
        if (context instanceof SubRoutingContext) {
            SubRoutingContext ctx = (SubRoutingContext) context;
            return new SubRoutingContext(ctx.mountPoint(), redirectPath, ctx.routes, ctx.inner);
        }
        return null;
    }
}
