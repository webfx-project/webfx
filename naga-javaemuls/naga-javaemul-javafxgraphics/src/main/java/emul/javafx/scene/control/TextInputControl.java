package emul.javafx.scene.control;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.beans.property.SimpleStringProperty;
import emul.javafx.beans.property.StringProperty;
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

    private final StringProperty textProperty = new SimpleStringProperty();
    @Override
    public StringProperty textProperty() {
        return textProperty;
    }

    private final Property<String> promptProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> promptTextProperty() {
        return promptProperty;
    }
}
