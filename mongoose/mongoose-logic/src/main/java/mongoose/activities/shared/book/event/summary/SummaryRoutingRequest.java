package mongoose.activities.shared.book.event.summary;

import mongoose.activities.shared.book.event.terms.TermsRouting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class SummaryRoutingRequest extends UiRoutingRequest {

    public SummaryRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, SummaryRouting.PATH), history);
    }
}
