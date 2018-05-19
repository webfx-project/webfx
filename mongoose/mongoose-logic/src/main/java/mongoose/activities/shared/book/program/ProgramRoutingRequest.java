package mongoose.activities.shared.book.program;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class ProgramRoutingRequest extends PushRoutingRequest {

    public ProgramRoutingRequest(Object eventId, History history) {
        super(ProgramRouting.getProgramPath(eventId), history);
    }

}
