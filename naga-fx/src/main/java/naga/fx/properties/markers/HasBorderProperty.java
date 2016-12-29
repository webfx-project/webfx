package naga.fx.properties.markers;

import javafx.beans.property.Property;
import naga.fx.scene.layout.Border;

/**
 * @author Bruno Salmon
 */
public interface HasBorderProperty {

    Property<Border> borderProperty();
    default void setBorder(Border border) { borderProperty().setValue(border); }
    default Border getBorder() { return borderProperty().getValue(); }

}
