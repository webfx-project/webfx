package mongoose.client.activity.eventdependent;

import mongoose.client.businessdata.feesgroup.FeesGroup;
import mongoose.client.aggregates.event.EventAggregate;
import mongoose.client.aggregates.event.EventAggregateMixin;
import mongoose.client.businessdata.feesgroup.FeesGroupsByEventStore;
import mongoose.client.businessdata.preselection.ActiveOptionsPreselectionsByEventStore;
import mongoose.client.businessdata.preselection.OptionsPreselection;
import mongoose.client.businessdata.workingdocument.ActiveWorkingDocumentsByEventStore;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContext;
import webfx.framework.client.activity.impl.elementals.domain.DomainActivityContextMixin;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContext;
import webfx.framework.client.activity.impl.elementals.uiroute.UiRouteActivityContextMixin;
import webfx.platform.shared.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface EventDependentActivityMixin
        <C extends DomainActivityContext<C> & UiRouteActivityContext<C>>

        extends UiRouteActivityContextMixin<C>,
        DomainActivityContextMixin<C>,
        EventAggregateMixin,
        EventDependentPresentationModelMixin
{

    default EventAggregate getEventService() {
        return EventAggregate.getOrCreate(getEventId(), getDataSourceModel());
    }

    default void updateEventDependentPresentationModelFromContextParameters() {
        setEventId(getParameter("eventId"));
        setOrganizationId(getParameter("organizationId"));
    }

    default WorkingDocument getEventActiveWorkingDocument() {
        return ActiveWorkingDocumentsByEventStore.getEventActiveWorkingDocument(this);
    }

    default Future<FeesGroup[]> onEventFeesGroups() {
        return FeesGroupsByEventStore.onEventFeesGroups(this);
    }

    default OptionsPreselection getEventActiveOptionsPreselection() {
        return ActiveOptionsPreselectionsByEventStore.getActiveOptionsPreselection(this);
    }

}
