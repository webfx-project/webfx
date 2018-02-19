package naga.framework.router.impl;

import naga.framework.router.RoutingContext;
import naga.framework.router.auth.authn.RedirectAuthHandler;
import naga.framework.router.auth.authz.RouteAuthorizationRequest;
import naga.framework.session.Session;
import naga.framework.spi.authz.AuthorizationService;
import naga.util.Booleans;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class RedirectAuthHandlerImpl extends AuthHandlerImpl implements RedirectAuthHandler {

    private final String loginPath;
    private final String unauthorizedPath;
    private boolean redirecting;

    public RedirectAuthHandlerImpl(String loginPath, String unauthorizedPath) {
        this.loginPath = loginPath;
        this.unauthorizedPath = unauthorizedPath;
    }

    @Override
    public void handle(RoutingContext context) {
        if (redirecting)
            context.next();
        else {
            Session session = context.session();
            if (session == null)
                context.fail(new IllegalStateException("No session - did you forget to include a SessionHandler?"));
            else {
                // Checking that the user has been authenticated
                Object userPrincipal = context.userPrincipal();
                if (userPrincipal == null) // If not, redirecting to the login path
                    redirect(context, loginPath);
                else { // If authenticated, we need to check the user is authorized to access this route
                    AuthorizationService authorizationService = ServiceLoaderHelper.loadService(AuthorizationService.class, ServiceLoaderHelper.NotFoundPolicy.TRACE_AND_RETURN_NULL);
                    if (authorizationService == null)
                        context.fail(new IllegalStateException("No AuthorizationService found"));
                    else
                        authorizationService.isAuthorized(new RouteAuthorizationRequest(context.path()), userPrincipal).setHandler(ar -> {
                            if (ar.failed())
                                context.fail(ar.cause());
                            else if (Booleans.isTrue(ar.result()))
                                context.next();
                            else
                                redirect(context, unauthorizedPath);
                        });
                }
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
