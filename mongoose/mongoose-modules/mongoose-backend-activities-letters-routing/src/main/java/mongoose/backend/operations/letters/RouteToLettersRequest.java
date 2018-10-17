package mongoose.backend.operations.letters;

import mongoose.backend.activities.letters.routing.LettersRouting;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.client.operations.route.RouteRequestEmitter;
import webfx.framework.shared.router.auth.authz.RouteRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToLettersRequest extends RoutePushRequest implements HasOperationCode {

    private final static String OPERATION_CODE = "RouteToLetters";

    public RouteToLettersRequest(Object eventId, BrowsingHistory history) {
        super(LettersRouting.getEventLettersPath(eventId), history);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    public static final class ProvidedEmitter implements RouteRequestEmitter {
        @Override
        public RouteRequest instantiateRouteRequest(UiRouteActivityContext context) {
            return new RouteToLettersRequest(context.getParameter("eventId"), context.getHistory());
        }
    }

}
