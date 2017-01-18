package naga.fx.properties.markers;

import emul.javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutYProperty {

    Property<Double> layoutYProperty();
    default void setLayoutY(Double layoutY) { layoutYProperty().setValue(layoutY); }
    default Double getLayoutY() { return layoutYProperty().getValue(); }

}
