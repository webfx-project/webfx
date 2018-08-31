package mongoose.operations.bothends.route;

import mongoose.activities.bothends.book.program.ProgramRouting;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public final class RouteToProgramRequest extends RoutePushRequest {

    public RouteToProgramRequest(Object eventId, History history) {
        super(ProgramRouting.getProgramPath(eventId), history);
    }

}
