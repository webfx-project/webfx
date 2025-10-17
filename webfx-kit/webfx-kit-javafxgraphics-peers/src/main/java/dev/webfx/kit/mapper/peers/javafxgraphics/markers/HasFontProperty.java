package dev.webfx.kit.mapper.peers.javafxgraphics.markers;

import javafx.beans.property.ObjectProperty;
import javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public interface HasFontProperty {

    ObjectProperty<Font> fontProperty();
    default void setFont(Font font) {
        fontProperty().setValue(font);
    }
    default Font getFont() {
        return fontProperty().getValue();
    }

}
