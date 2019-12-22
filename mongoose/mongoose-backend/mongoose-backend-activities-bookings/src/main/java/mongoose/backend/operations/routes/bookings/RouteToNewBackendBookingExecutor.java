package mongoose.backend.operations.routes.bookings;

import mongoose.client.aggregates.event.EventAggregate;
import mongoose.frontend.operations.fees.RouteToFeesRequest;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
final class RouteToNewBackendBookingExecutor {

    static Future<Void> executeRequest(RouteToNewBackendBookingRequest rq) {
        return execute(rq.getEventId(), rq.getHistory());
    }

    private static Future<Void> execute(Object eventId, BrowsingHistory history) {
        // When made in the backend, we don't want to add the new booking to the last visited booking cart (as
        // opposed to the frontend), so we clear the reference to the active booking cart (if set) before routing
        EventAggregate eventAggregate = EventAggregate.get(eventId);
        if (eventAggregate != null)
            eventAggregate.setActiveCart(null);
        // Now that the current cart reference is cleared, we can route to the fees page
        new RouteToFeesRequest(eventId, history).execute();
        return Future.succeededFuture();
    }
}
