package naga.core.spi.gui.node;


import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface SelectedProperty {

    Property<Boolean> selectedProperty();
    default void setSelected(boolean selected) { selectedProperty().setValue(selected); }
    default boolean isSelected() { return selectedProperty().getValue(); }

}
