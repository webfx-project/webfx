package mongoose.activities.shared.book.event.terms;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class TermsRoutingRequest extends PushRoutingRequest {

    public TermsRoutingRequest(Object eventId, History history) {
        super(TermsRouting.getTermsPath(eventId), history);
    }

}
