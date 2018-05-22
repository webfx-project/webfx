package mongoose.operations.shared.route;

import mongoose.activities.shared.book.program.ProgramRouting;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToProgramRequest extends RoutePushRequest {

    public RouteToProgramRequest(Object eventId, History history) {
        super(ProgramRouting.getProgramPath(eventId), history);
    }

}
