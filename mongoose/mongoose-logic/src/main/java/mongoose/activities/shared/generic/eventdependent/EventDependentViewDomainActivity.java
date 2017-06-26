package mongoose.activities.shared.generic.eventdependent;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import mongoose.activities.shared.generic.MongooseButtonFactoryMixin;
import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.activity.view.impl.ViewActivityImpl;

/**
 * @author Bruno Salmon
 */
public abstract class EventDependentViewDomainActivity

    extends ViewActivityImpl
    implements EventDependentMixin<ViewDomainActivityContextFinal>,
        MongooseButtonFactoryMixin {

    private final Property<Object> eventIdProperty = new SimpleObjectProperty<>();
    @Override
    public ObservableValue<Object> eventIdProperty() {
        return eventIdProperty;
    }

    @Override
    protected void fetchRouteParameters() {
        eventIdProperty.setValue(getParameter("eventId"));
        super.fetchRouteParameters();
    }
}
