package mongoose.activities.bothends.generic.eventdependent;

import mongoose.aggregates.EventAggregate;
import mongoose.aggregates.EventAggregateMixin;
import naga.framework.activity.base.elementals.domain.DomainActivityContext;
import naga.framework.activity.base.elementals.domain.DomainActivityContextMixin;
import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContext;
import naga.framework.activity.base.elementals.uiroute.UiRouteActivityContextMixin;

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

}
