package mongoose.activities.shared.book.event.program;

import mongoose.activities.shared.book.event.terms.TermsRouting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class ProgramRoutingRequest extends UiRoutingRequest {

    public ProgramRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, ProgramRouting.PATH), history);
    }
}
