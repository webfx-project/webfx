package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.terms.TermsRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToTermsRequest extends RoutePushRequest {

    public RouteToTermsRequest(Object eventId, History history) {
        super(TermsRouting.getTermsPath(eventId), history);
    }

}
