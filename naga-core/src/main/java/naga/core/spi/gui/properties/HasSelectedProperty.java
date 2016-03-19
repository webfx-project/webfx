package naga.core.spi.gui.properties;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasSelectedProperty {

    Property<Boolean> selectedProperty();
    default void setSelected(boolean selected) { selectedProperty().setValue(selected); }
    default boolean isSelected() { return selectedProperty().getValue(); }

}
