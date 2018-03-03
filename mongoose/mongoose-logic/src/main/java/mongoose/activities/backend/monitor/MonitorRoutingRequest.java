package mongoose.activities.backend.monitor;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class MonitorRoutingRequest extends PushRoutingRequest {

    public MonitorRoutingRequest(History history) {
        super(MonitorRouting.PATH, history);
    }
}
