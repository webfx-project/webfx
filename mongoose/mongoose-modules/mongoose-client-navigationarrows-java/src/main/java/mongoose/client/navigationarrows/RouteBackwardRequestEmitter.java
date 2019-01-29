package mongoose.client.navigationarrows;

import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.operations.route.RouteBackwardRequest;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.router.auth.authz.RouteRequest;

/**
 * @author Bruno Salmon
 */
public final class RouteBackwardRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteBackwardRequest(context.getHistory());
    }
}
