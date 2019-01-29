package mongoose.client.navigationarrows;

import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.operations.route.RouteForwardRequest;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.router.auth.authz.RouteRequest;

/**
 * @author Bruno Salmon
 */
public final class RouteForwardRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteForwardRequest(context.getHistory());
    }
}
