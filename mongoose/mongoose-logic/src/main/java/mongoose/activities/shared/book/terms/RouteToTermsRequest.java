package mongoose.activities.shared.book.terms;

import naga.framework.ui.router.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToTermsRequest extends PushRouteRequest {

    public RouteToTermsRequest(Object eventId, History history) {
        super(TermsRouting.getTermsPath(eventId), history);
    }

}
