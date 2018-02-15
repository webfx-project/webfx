package mongoose.activities.shared.book.event.options;

import mongoose.activities.shared.generic.routing.MongooseRoutingUtil;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.services.EventService;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.orm.entity.Entity;
import naga.framework.ui.router.UiRoute;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class OptionsRooting {

    public static final String PATH = "/book/event/:eventId/options";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , OptionsViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static void routeUsingEvent(Entity event, History history) {
        MongooseRoutingUtil.routeUsingEntityId(event, history, OptionsRooting::routeUsingEventId);
    }

    public static void routeUsingEventId(Object eventId, History history) {
        history.push(MongooseRoutingUtil.interpolateEventIdInPath(eventId, PATH));
    }

    public static void routeUsingOptionsPreselection(OptionsPreselection optionsPreselection, History history) {
        routeUsingWorkingDocument(optionsPreselection.getWorkingDocument(), optionsPreselection, history);
    }

    public static void routeUsingWorkingDocument(WorkingDocument workingDocument, History history) {
        routeUsingWorkingDocument(workingDocument, null, history);
    }

    private static void routeUsingWorkingDocument(WorkingDocument workingDocument, OptionsPreselection optionsPreselection, History history) {
        EventService eventService = workingDocument.getEventService();
        eventService.setSelectedOptionsPreselection(optionsPreselection);
        eventService.setWorkingDocument(optionsPreselection == null ? workingDocument : null);
        Object eventId = workingDocument.getDocument().getEventId();
        if (eventId == null)
            eventId = eventService.getEvent().getId();
        routeUsingEventId(eventId, history);
    }
}
