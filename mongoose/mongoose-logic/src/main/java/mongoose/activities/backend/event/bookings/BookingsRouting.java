package mongoose.activities.backend.event.bookings;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.router.util.PathBuilder;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

import java.time.LocalDate;

/**
 * @author Bruno Salmon
 */
public class BookingsRouting {

    // Would be better but pb retrieving named groups doesn't work with JavaScript RegExp (can't retrieve groups): private final static String ANY_PATH = "/bookings(/organization/:organizationId|/event/:eventId|/day/:day|/arrivals|/departures|/minday/:minDay|/maxday/:maxDay|/filter/:filter|/groupby|:groupBy|/orderby/:orderBy/columns/:columns)*";
    private final static String ANY_PATH = "/bookings(/organization/:organizationId)?(/event/:eventId)?(/day/:day)?(/arrivals)?(/departures)?(/minday/:minDay)?(/maxday/:maxDay)?(/filter/:filter)?(/groupby/:groupBy)?(/orderby/:orderBy)?(/columns/:columns)?";
    private final static String EVENT_PATH = "/bookings/event/:eventId";

    public static UiRoute<?> uiRoute() {
        return UiRoute.createRegex(PathBuilder.toRegexPath(ANY_PATH)
                ,true
                , BookingsPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityId(event, history, BookingsRouting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, EVENT_PATH));
    }

    public static LocalDate parseDayParam(String parameterValue) {
        if (parameterValue == null)
            return null;
        switch (parameterValue) {
            case "yesterday" : return LocalDate.now().minusDays(1);
            case "today": return LocalDate.now();
            case "tomorrow" : return LocalDate.now().plusDays(1);
            default: return LocalDate.parse(parameterValue); // Expecting an iso date (yyyy-MM-dd)
        }
    }
}
