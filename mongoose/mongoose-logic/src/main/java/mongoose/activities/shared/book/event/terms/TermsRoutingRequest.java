package mongoose.activities.shared.book.event.terms;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class TermsRoutingRequest extends UiRoutingRequest {

    public TermsRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, TermsRouting.PATH), history);
    }
}
