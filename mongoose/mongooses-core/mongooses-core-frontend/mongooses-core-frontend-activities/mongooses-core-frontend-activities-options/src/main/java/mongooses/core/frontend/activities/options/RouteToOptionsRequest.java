package mongooses.core.frontend.activities.options;

import mongooses.core.sharedends.businesslogic.preselection.OptionsPreselection;
import mongooses.core.sharedends.businesslogic.workingdocument.WorkingDocument;
import mongooses.core.sharedends.aggregates.EventAggregate;
import webfx.framework.operations.route.RoutePushRequest;
import webfx.platforms.core.services.windowhistory.spi.BrowsingHistory;

/**
 * @author Bruno Salmon
 */
public final class RouteToOptionsRequest extends RoutePushRequest {

    public RouteToOptionsRequest(Object eventId, BrowsingHistory history) {
        super(OptionsRouting.getEventOptionsPath(eventId), history);
    }

    public RouteToOptionsRequest(WorkingDocument workingDocument, BrowsingHistory history) {
        this(workingDocument, null, history);
    }

    public RouteToOptionsRequest(OptionsPreselection optionsPreselection, BrowsingHistory history) {
        this(optionsPreselection.getWorkingDocument(), optionsPreselection, history);
    }

    public RouteToOptionsRequest(WorkingDocument workingDocument, OptionsPreselection optionsPreselection, BrowsingHistory history) {
        this(prepareEventServiceAndReturnEventId(workingDocument, optionsPreselection), history);
    }

    private static Object prepareEventServiceAndReturnEventId(WorkingDocument workingDocument, OptionsPreselection optionsPreselection) {
        EventAggregate eventAggregate = workingDocument.getEventAggregate();
        eventAggregate.setSelectedOptionsPreselection(optionsPreselection);
        eventAggregate.setWorkingDocument(optionsPreselection == null ? workingDocument : null);
        Object eventId = workingDocument.getDocument().getEventId();
        if (eventId == null)
            eventId = eventAggregate.getEvent().getId();
        return eventId;
    }

}
