package mongoose.client.activity.eventdependent;

import javafx.beans.property.ObjectProperty;

/**
 * @author Bruno Salmon
 */
public interface EventDependentPresentationModelMixin extends EventDependentPresentationModel {

    EventDependentPresentationModel getPresentationModel();

    @Override
    default ObjectProperty<Object> eventIdProperty() {
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
    default ObjectProperty<Object> organizationIdProperty() {
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
