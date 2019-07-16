package mongoose.backend.activities.diningareas;

import mongoose.backend.operations.routes.diningareas.RouteToDiningAreasRequest;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.router.auth.authz.RouteRequest;

/**
 * @author Bruno Salmon
 */
public final class RouteToDiningAreasRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteToDiningAreasRequest(context.getParameter("eventId"), context.getHistory());
    }
}
