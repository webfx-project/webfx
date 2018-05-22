package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.terms.TermsRouting;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToTermsRequest extends RoutePushRequest {

    public RouteToTermsRequest(Object eventId, History history) {
        super(TermsRouting.getTermsPath(eventId), history);
    }

}
