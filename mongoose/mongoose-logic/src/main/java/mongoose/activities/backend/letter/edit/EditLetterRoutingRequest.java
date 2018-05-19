package mongoose.activities.backend.letter.edit;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class EditLetterRoutingRequest extends PushRoutingRequest {

    public EditLetterRoutingRequest(Object letterId, History history) {
        super(EditLetterRouting.getEditLetterPath(letterId), history);
    }

}
