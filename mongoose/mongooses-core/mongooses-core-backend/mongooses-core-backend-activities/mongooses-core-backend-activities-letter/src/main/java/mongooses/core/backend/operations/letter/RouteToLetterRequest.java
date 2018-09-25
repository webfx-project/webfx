package mongooses.core.backend.operations.letter;

import mongooses.core.backend.activities.letter.LetterRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToLetterRequest extends RoutePushRequest {

    public RouteToLetterRequest(Object letterId, BrowsingHistory history) {
        super(LetterRouting.getEditLetterPath(letterId), history);
    }

}
