package mongoose.operations.shared.route;

import mongoose.activities.shared.book.options.OptionsRouting;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.services.EventService;
import naga.framework.operations.route.RoutePushRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class RouteToOptionsRequest extends RoutePushRequest {

    public RouteToOptionsRequest(Object eventId, History history) {
        super(OptionsRouting.getEventOptionsPath(eventId), history);
    }

    public RouteToOptionsRequest(WorkingDocument workingDocument, History history) {
        this(workingDocument, null, history);
    }

    public RouteToOptionsRequest(OptionsPreselection optionsPreselection, History history) {
        this(optionsPreselection.getWorkingDocument(), optionsPreselection, history);
    }

    public RouteToOptionsRequest(WorkingDocument workingDocument, OptionsPreselection optionsPreselection, History history) {
        this(prepareEventServiceAndReturnEventId(workingDocument, optionsPreselection), history);
    }

    private static Object prepareEventServiceAndReturnEventId(WorkingDocument workingDocument, OptionsPreselection optionsPreselection) {
        EventService eventService = workingDocument.getEventService();
        eventService.setSelectedOptionsPreselection(optionsPreselection);
        eventService.setWorkingDocument(optionsPreselection == null ? workingDocument : null);
        Object eventId = workingDocument.getDocument().getEventId();
        if (eventId == null)
            eventId = eventService.getEvent().getId();
        return eventId;
    }

}
