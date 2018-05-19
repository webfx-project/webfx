package mongoose.activities.shared.book.fees;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class FeesRoutingRequest extends PushRoutingRequest {

    public FeesRoutingRequest(Object eventId, History history) {
        super(FeesRouting.getFeesPath(eventId), history);
    }

}
