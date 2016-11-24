package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.geometry.Pos;

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
