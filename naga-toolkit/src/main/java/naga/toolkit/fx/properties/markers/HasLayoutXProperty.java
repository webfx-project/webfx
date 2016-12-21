package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;

/**
 * @author Bruno Salmon
 */
public interface HasLayoutXProperty {

    Property<Double> layoutXProperty();
    default void setLayoutX(Double layoutX) { layoutXProperty().setValue(layoutX); }
    default Double getLayoutX() { return layoutXProperty().getValue(); }

}
