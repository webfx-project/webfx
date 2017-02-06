package mongoose.activities.shared.generic.eventdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import naga.commons.util.function.Factory;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityContextFinal;
import naga.framework.activity.combinations.domainpresentationlogic.impl.DomainPresentationLogicActivityImpl;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentPresentationLogicActivity
        <PM extends EventDependentPresentationModel>

        extends DomainPresentationLogicActivityImpl<PM>
        implements EventDependentMixin<DomainPresentationLogicActivityContextFinal<PM>> {

    public EventDependentPresentationLogicActivity(Factory<PM> presentationModelFactory) {
        super(presentationModelFactory);
    }

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();

    @Override
    public ObservableValue<Object> eventIdProperty() {
        return eventIdProperty;
    }

    protected void initializePresentationModel(PM pm) {
        pm.eventIdProperty().bind(eventIdProperty());
    }

    @Override
    protected void fetchRouteParameters() {
        eventIdProperty.setValue(getParameter("eventId"));
        super.fetchRouteParameters();
    }

}
