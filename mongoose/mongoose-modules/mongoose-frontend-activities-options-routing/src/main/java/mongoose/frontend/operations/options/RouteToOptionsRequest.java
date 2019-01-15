package mongoose.frontend.operations.options;

import mongoose.client.businesslogic.preselection.OptionsPreselection;
import mongoose.client.businesslogic.workingdocument.WorkingDocument;
import mongoose.frontend.activities.options.routing.OptionsRouting;
import webfx.framework.client.operations.route.RoutePushRequest;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.platform.client.services.windowhistory.spi.BrowsingHistory;

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

    private static EntityId prepareEventServiceAndReturnEventId(WorkingDocument workingDocument, OptionsPreselection optionsPreselection) {
        EntityId eventId = workingDocument.getDocument().getEventId();
        if (eventId == null)
            eventId = workingDocument.getEventAggregate().getEvent().getId();
        OptionsPreselection.setSelectedOptionsPreselection(optionsPreselection, eventId);
        WorkingDocument.setEventActiveWorkingDocument(optionsPreselection == null ? workingDocument : null, eventId);
        return eventId;
    }

}
