package mongoose.operations.backend.route;

import mongoose.activities.backend.letter.LetterRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToLetterRequest extends RoutePushRequest {

    public RouteToLetterRequest(Object letterId, History history) {
        super(LetterRouting.getEditLetterPath(letterId), history);
    }

}
