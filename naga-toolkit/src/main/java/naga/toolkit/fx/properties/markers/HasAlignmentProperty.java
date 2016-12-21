package naga.toolkit.fx.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.geometry.Pos;

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
