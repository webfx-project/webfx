package mongoose.activities.backend.monitor;

import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class MonitorRoutingRequest extends UiRoutingRequest {

    public MonitorRoutingRequest(History history) {
        super(MonitorRouting.PATH, history);
    }
}
