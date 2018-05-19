package mongoose.activities.shared.book.event.options;

import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.services.EventService;
import naga.framework.ui.router.PushRoutingRequest;
import naga.platform.client.url.history.History;

/**
 * @author Bruno Salmon
 */
public class OptionsRoutingRequest extends PushRoutingRequest {

    public OptionsRoutingRequest(Object eventId, History history) {
        super(OptionsRouting.getEventOptionsPath(eventId), history);
    }

    public OptionsRoutingRequest(WorkingDocument workingDocument, History history) {
        this(workingDocument, null, history);
    }

    public OptionsRoutingRequest(OptionsPreselection optionsPreselection, History history) {
        this(optionsPreselection.getWorkingDocument(), optionsPreselection, history);
    }

    public OptionsRoutingRequest(WorkingDocument workingDocument, OptionsPreselection optionsPreselection, History history) {
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
