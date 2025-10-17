package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public interface HasTextAlignmentProperty {

    ObjectProperty<TextAlignment> textAlignmentProperty();
    default void setTextAlignment(TextAlignment textAlignment) {
        textAlignmentProperty().setValue(textAlignment);
    }
    default TextAlignment getTextAlignment() {
        return textAlignmentProperty().getValue();
    }

}
