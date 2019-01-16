package mongoose.client.activity.eventdependent;

import mongoose.client.businesslogic.fees.FeesGroup;
import mongoose.client.aggregates.event.EventAggregate;
import mongoose.client.aggregates.event.EventAggregateMixin;
import mongoose.client.businesslogic.fees.FeesGroupsByEventStore;
import mongoose.client.businesslogic.preselection.OptionsPreselection;
import mongoose.client.businesslogic.workingdocument.ActiveWorkingDocumentsByEventStore;
import mongoose.client.businesslogic.workingdocument.WorkingDocument;
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

    default void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection) {
        OptionsPreselection.setSelectedOptionsPreselection(selectedOptionsPreselection, this);
    }

    default OptionsPreselection getSelectedOptionsPreselection() {
        return OptionsPreselection.getSelectedOptionsPreselection(this);
    }

}
