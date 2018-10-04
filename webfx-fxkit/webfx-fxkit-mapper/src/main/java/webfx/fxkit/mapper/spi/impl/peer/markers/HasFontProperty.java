package webfx.fxkit.mapper.spi.impl.peer.markers;

import javafx.beans.property.Property;
import javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public interface HasFontProperty {

    Property<Font> fontProperty();
    default void setFont(Font font) {
        fontProperty().setValue(font);
    }
    default Font getFont() {
        return fontProperty().getValue();
    }

}
