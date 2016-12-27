package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.scene.layout.Border;

/**
 * @author Bruno Salmon
 */
public interface HasBorderProperty {

    Property<Border> borderProperty();
    default void setBorder(Border border) { borderProperty().setValue(border); }
    default Border getBorder() { return borderProperty().getValue(); }

}
