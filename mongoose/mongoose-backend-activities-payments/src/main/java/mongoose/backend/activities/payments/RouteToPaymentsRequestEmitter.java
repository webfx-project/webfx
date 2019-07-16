package mongoose.backend.activities.payments;

import mongoose.backend.operations.routes.payments.RouteToPaymentsRequest;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.router.auth.authz.RouteRequest;

/**
 * @author Bruno Salmon
 */
public final class RouteToPaymentsRequestEmitter implements RouteRequestEmitter {

    @Override
    public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
        return new RouteToPaymentsRequest(context.getParameter("eventId"), context.getHistory());
    }
}
