package mongoose.activities.shared.book.event.person;

import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class PersonRoutingRequest extends PushRoutingRequest {

    public PersonRoutingRequest(Object eventId, History history) {
        super(PersonRouting.getPersonPath(eventId), history);
    }

}
