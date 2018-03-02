package mongoose.activities.backend.event.bookings;

import naga.framework.ui.router.UiRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class BookingsRoutingRequest extends UiRoutingRequest {

    private Object eventId;

    public BookingsRoutingRequest(Object eventId, History history) {
        setEventId(eventId);
        setHistory(history);
    }

    public Object getEventId() {
        return eventId;
    }

    public BookingsRoutingRequest setEventId(Object eventId) {
        this.eventId = eventId;
        return this;
    }

    @Override
    public String getRoutePath() {
        setRoutePath(BookingsRouting.getEventIdRoutePath(eventId));
        return super.getRoutePath();
    }

}
