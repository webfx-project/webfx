package naga.fx.properties.markers;

import javafx.beans.property.Property;
import naga.fx.scene.text.Font;

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
