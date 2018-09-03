package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutYProperty {

    DoubleProperty layoutYProperty();
    default void setLayoutY(Double layoutY) { layoutYProperty().setValue(layoutY); }
    default Double getLayoutY() { return layoutYProperty().getValue(); }

}
