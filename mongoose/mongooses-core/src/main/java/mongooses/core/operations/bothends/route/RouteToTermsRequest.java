package mongooses.core.operations.bothends.route;

import mongooses.core.activities.sharedends.book.terms.TermsRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToTermsRequest extends RoutePushRequest {

    public RouteToTermsRequest(Object eventId, History history) {
        super(TermsRouting.getTermsPath(eventId), history);
    }

}
