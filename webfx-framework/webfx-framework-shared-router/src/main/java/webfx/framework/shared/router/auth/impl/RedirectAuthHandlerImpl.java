package webfx.framework.shared.router.auth.impl;

import webfx.framework.shared.router.RoutingContext;
import webfx.framework.shared.router.auth.RedirectAuthHandler;
import webfx.framework.shared.router.auth.authz.RouteRequest;
import webfx.framework.shared.router.impl.RoutingContextImplBase;
import webfx.framework.shared.services.authz.AuthorizationRequest;

/**
 * @author Bruno Salmon
 */
public final class RedirectAuthHandlerImpl implements RedirectAuthHandler {

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
                    .setOperationRequest(new RouteRequest(requestedPath))
                    .onAuthorizedExecute(context::next)
                    .onUnauthorizedExecute(() -> redirectToAuth(context))
                    .executeAsync();
    }

    private void redirectToAuth(RoutingContext context) {
        RoutingContext authContext = RoutingContextImplBase.newRedirectedContext(context, context.userPrincipal() == null ? loginPath : unauthorizedPath);
        authContext.next();
    }
}
