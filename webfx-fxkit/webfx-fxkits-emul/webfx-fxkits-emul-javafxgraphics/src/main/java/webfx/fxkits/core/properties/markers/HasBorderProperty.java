package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.layout.Border;

/**
 * @author Bruno Salmon
 */
public interface HasBorderProperty {

    Property<Border> borderProperty();
    default void setBorder(Border border) { borderProperty().setValue(border); }
    default Border getBorder() { return borderProperty().getValue(); }

}
