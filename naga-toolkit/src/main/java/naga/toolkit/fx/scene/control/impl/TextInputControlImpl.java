package naga.toolkit.fx.scene.control.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.control.TextInputControl;
import naga.toolkit.fx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
abstract class TextInputControlImpl extends ControlImpl implements TextInputControl {

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
