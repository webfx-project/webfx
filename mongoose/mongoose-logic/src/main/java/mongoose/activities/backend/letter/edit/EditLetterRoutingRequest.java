package mongoose.activities.backend.letter.edit;

import mongoose.activities.backend.events.EventsRouting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class EditLetterRoutingRequest extends UiRoutingRequest {

    public EditLetterRoutingRequest(Object letterId, History history) {
        super(MongooseRoutingUtil.interpolateLetterIdInPath(letterId, EditLetterRouting.PATH), history);
    }

}
