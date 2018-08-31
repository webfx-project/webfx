package webfx.fx.properties.markers;

import emul.javafx.beans.property.DoubleProperty;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutXProperty {

    DoubleProperty layoutXProperty();
    default void setLayoutX(Double layoutX) { layoutXProperty().setValue(layoutX); }
    default Double getLayoutX() { return layoutXProperty().getValue(); }

}
