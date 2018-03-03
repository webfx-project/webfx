package mongoose.activities.shared.book.event.person;

import mongoose.activities.shared.book.event.program.ProgramRouting;
import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class PersonRoutingRequest extends UiRoutingRequest {

    public PersonRoutingRequest(Object eventId, History history) {
        super(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PersonRouting.PATH), history);
    }
}
