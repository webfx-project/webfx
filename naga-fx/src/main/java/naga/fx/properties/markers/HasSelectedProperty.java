package naga.fx.properties.markers;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasSelectedProperty {

    Property<Boolean> selectedProperty();
    default void setSelected(Boolean selected) { selectedProperty().setValue(selected); }
    default Boolean isSelected() { return selectedProperty().getValue(); }

}
