package webfx.fx.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.geometry.Insets;

/**
 * @author Bruno Salmon
 */
public interface HasPaddingProperty {

    Property<Insets> paddingProperty();
    default void setPadding(Insets insets) {
        paddingProperty().setValue(insets);
    }
    default Insets getPadding() {
        return paddingProperty().getValue();
    }

}
