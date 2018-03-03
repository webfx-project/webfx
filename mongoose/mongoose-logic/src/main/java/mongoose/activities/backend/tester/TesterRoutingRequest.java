package mongoose.activities.backend.tester;

import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class TesterRoutingRequest extends UiRoutingRequest {

    public TesterRoutingRequest(History history) {
        super(TesterRouting.PATH, history);
    }

}
