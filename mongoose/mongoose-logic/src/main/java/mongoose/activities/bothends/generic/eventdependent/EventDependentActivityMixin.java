package mongoose.activities.bothends.generic.eventdependent;

import mongoose.services.EventService;
import mongoose.services.EventServiceMixin;
import naga.framework.activity.domain.DomainActivityContext;
import naga.framework.activity.domain.DomainActivityContextMixin;
import naga.framework.activity.uiroute.UiRouteActivityContext;
import naga.framework.activity.uiroute.UiRouteActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface EventDependentActivityMixin
        <C extends DomainActivityContext<C> & UiRouteActivityContext<C>>

        extends UiRouteActivityContextMixin<C>,
        DomainActivityContextMixin<C>,
        EventServiceMixin,
        EventDependentPresentationModelMixin
{

    default EventService getEventService() {
        return EventService.getOrCreate(getEventId(), getDataSourceModel());
    }

    default void updateEventDependentPresentationModelFromContextParameters() {
        setEventId(getParameter("eventId"));
        setOrganizationId(getParameter("organizationId"));
    }

}
