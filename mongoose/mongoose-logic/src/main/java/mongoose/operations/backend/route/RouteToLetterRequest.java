package mongoose.operations.backend.route;

import mongoose.activities.backend.letter.LetterRouting;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToLetterRequest extends RoutePushRequest {

    public RouteToLetterRequest(Object letterId, History history) {
        super(LetterRouting.getEditLetterPath(letterId), history);
    }

}
