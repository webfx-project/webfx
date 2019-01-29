package mongoose.backend.operations.events;

import mongoose.backend.activities.events.routing.EventsRouting;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.router.auth.authz.RouteRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToEventsRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToEvents";

    public RouteToEventsRequest(BrowsingHistory history) {
        super(EventsRouting.getAllEventsPath(), history);
    }

    public RouteToEventsRequest(Object organizationId, BrowsingHistory history) {
        super(EventsRouting.getOrganizationEventsPath(organizationId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
