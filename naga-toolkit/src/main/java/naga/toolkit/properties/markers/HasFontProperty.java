package naga.toolkit.properties.markers;

import javafx.beans.property.Property;
import naga.toolkit.fx.text.Font;

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
