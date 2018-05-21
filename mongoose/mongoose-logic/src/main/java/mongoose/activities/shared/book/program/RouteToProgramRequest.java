package mongoose.activities.shared.book.program;

import naga.framework.ui.router.PushRouteRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToProgramRequest extends PushRouteRequest {

    public RouteToProgramRequest(Object eventId, History history) {
        super(ProgramRouting.getProgramPath(eventId), history);
    }

}
