package webfx.fxkits.core.properties.markers;

import emul.javafx.beans.property.Property;
import emul.javafx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public interface HasTextAlignmentProperty {

    Property<TextAlignment> textAlignmentProperty();
    default void setTextAlignment(TextAlignment textAlignment) {
        textAlignmentProperty().setValue(textAlignment);
    }
    default TextAlignment getTextAlignment() {
        return textAlignmentProperty().getValue();
    }

}
