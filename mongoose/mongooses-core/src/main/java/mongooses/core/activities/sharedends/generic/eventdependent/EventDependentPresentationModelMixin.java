package mongooses.core.activities.sharedends.generic.eventdependent;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface EventDependentPresentationModelMixin extends EventDependentPresentationModel {

    EventDependentPresentationModel getPresentationModel();

    @Override
    default Property<Object> eventIdProperty() {
        return getPresentationModel().eventIdProperty();
    }

    @Override
    default void setEventId(Object eventId) {
        getPresentationModel().setEventId(eventId);
    }

    @Override
    default Object getEventId() {
        return getPresentationModel().getEventId();
    }

    @Override
    default Property<Object> organizationIdProperty() {
        return getPresentationModel().organizationIdProperty();
    }

    @Override
    default void setOrganizationId(Object organizationId) {
        getPresentationModel().setOrganizationId(organizationId);
    }

    @Override
    default Object getOrganizationId() {
        return getPresentationModel().getOrganizationId();
    }
}
