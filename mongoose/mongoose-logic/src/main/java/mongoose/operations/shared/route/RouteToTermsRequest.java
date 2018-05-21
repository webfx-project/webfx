package mongoose.operations.shared.route;

import mongoose.activities.shared.book.terms.TermsRouting;
import naga.framework.operations.route.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToTermsRequest extends PushRouteRequest {

    public RouteToTermsRequest(Object eventId, History history) {
        super(TermsRouting.getTermsPath(eventId), history);
    }

}
