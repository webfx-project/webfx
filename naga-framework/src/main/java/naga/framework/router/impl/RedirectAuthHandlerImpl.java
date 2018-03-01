package naga.framework.router.impl;

import naga.framework.router.RoutingContext;
import naga.framework.router.auth.RedirectAuthHandler;
import naga.framework.router.auth.authz.RouteAuthorizationRequest;
import naga.framework.spi.authz.AuthorizationRequest;

/**
 * @author Bruno Salmon
 */
public class RedirectAuthHandlerImpl implements RedirectAuthHandler {

    private final String loginPath;
    private final String unauthorizedPath;

    public RedirectAuthHandlerImpl(String loginPath, String unauthorizedPath) {
        this.loginPath = loginPath;
        this.unauthorizedPath = unauthorizedPath;
    }

    @Override
    public void handle(RoutingContext context) {
        String requestedPath = context.path();
        if (requestedPath.equals(loginPath) || requestedPath.equals(unauthorizedPath))
            context.next(); // Always accepting login and unauthorized paths
        else // Otherwise continuing the route only if the user is authorized, otherwise redirecting to auth page (login or unauthorized)
            new AuthorizationRequest<>()
                    .setUserPrincipal(context.userPrincipal())
                    .setOperationAuthorizationRequest(new RouteAuthorizationRequest(requestedPath))
                    .onAuthorizedExecute(context::next)
                    .onUnauthorizedExecute(() -> redirectToAuth(context))
                    .executeAsync();
    }

    private void redirectToAuth(RoutingContext context) {
        newRedirectedContext(context, context.userPrincipal() == null ? loginPath : unauthorizedPath).next();
    }

    private static RoutingContext newRedirectedContext(RoutingContext context, String redirectPath) {
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
