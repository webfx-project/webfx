package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.drawing.text.TextAlignment;

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
