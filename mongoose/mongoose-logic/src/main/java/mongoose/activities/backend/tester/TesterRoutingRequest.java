package mongoose.activities.backend.tester;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class TesterRoutingRequest extends PushRoutingRequest {

    public TesterRoutingRequest(History history) {
        super(TesterRouting.PATH, history);
    }

}
