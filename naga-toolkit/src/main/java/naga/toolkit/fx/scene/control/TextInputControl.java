package naga.toolkit.fx.scene.control;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.properties.markers.HasFontProperty;
import naga.toolkit.fx.properties.markers.HasPromptTextProperty;
import naga.toolkit.fx.properties.markers.HasTextProperty;

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
