package emul.javafx.scene.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.scene.text.Font;
import naga.fx.properties.markers.HasFontProperty;
import naga.fx.properties.markers.HasPromptTextProperty;
import naga.fx.properties.markers.HasTextProperty;

/**
 * @author Bruno Salmon
 */
public abstract class TextInputControl extends Control implements
        HasFontProperty,
        HasTextProperty,
        HasPromptTextProperty {

    private final Property<Font> fontProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Font> fontProperty() {
        return fontProperty;
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

    private final Property<String> promptProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> promptTextProperty() {
        return promptProperty;
    }
}
