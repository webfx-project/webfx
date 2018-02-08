package mongoose.activities.shared.book.event.options;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.services.EventService;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class OptionsRooting {

    public static final String PATH = "/book/event/:eventId/options";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH, false,
                false, OptionsViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityPrimaryKey(event, history, OptionsRooting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH));
    }

    public static void routeUsingWorkingDocument(WorkingDocument workingDocument, History history) {
        EventService eventService = workingDocument.getEventService();
        eventService.setSelectedOptionsPreselection(null);
        eventService.setWorkingDocument(workingDocument);
        routeUsingEventId(workingDocument.getDocument().getEventId(), history);
    }
}
