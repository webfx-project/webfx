package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.geometry.Pos;

/**
 * @author Bruno Salmon
 */
public interface HasAlignmentProperty {

    Property<Pos> alignmentProperty();
    default void setAlignment(Pos alignment) {
        alignmentProperty().setValue(alignment);
    }
    default Pos getAlignment() {
        return alignmentProperty().getValue();
    }

}
