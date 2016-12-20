package naga.toolkit.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.properties.markers.HasSelectedProperty;

/**
 * @author Bruno Salmon
 */
public class CheckBox extends ButtonBase implements
        HasSelectedProperty {

    private final Property<Boolean> selectedProperty = new SimpleObjectProperty<>(false);
    @Override
    public Property<Boolean> selectedProperty() {
        return selectedProperty;
    }
}
