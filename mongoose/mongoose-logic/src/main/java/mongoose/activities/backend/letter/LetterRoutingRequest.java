package mongoose.activities.backend.letter;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class LetterRoutingRequest extends PushRoutingRequest {

    public LetterRoutingRequest(Object letterId, History history) {
        super(LetterRouting.getEditLetterPath(letterId), history);
    }

}
