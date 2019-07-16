package mongoose.backend.activities.letters;

import mongoose.backend.operations.routes.letters.RouteToLettersRequest;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.router.auth.authz.RouteRequest;

/**
 * @author Bruno Salmon
 */
public final class RouteToLettersRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteToLettersRequest(context.getParameter("eventId"), context.getHistory());
    }
}
