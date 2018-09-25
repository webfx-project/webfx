package webfx.fxkits.extra.cell.renderer;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import webfx.platforms.core.util.Strings;
import java.util.function.BiFunction;

/**
 * @author Bruno Salmon
 */
public final class TextRenderer implements ValueRenderer {

    public final static TextRenderer SINGLETON = new TextRenderer();

    private BiFunction<Object, Object, TextField> textFieldFactory = (labelKey, placeholderKey) -> new TextField();

    private TextRenderer() {}

    public void setTextFieldFactory(BiFunction<Object, Object, TextField> textFieldFactory) {
        this.textFieldFactory = textFieldFactory;
    }

    @Override
    public Node renderValue(Object value, ValueRenderingContext context) {
        String stringValue = Strings.toSafeString(value);
        if (context.isReadOnly())
            return new Text(stringValue);
        TextField textField = textFieldFactory.apply(context.getLabelKey(), context.getPlaceholderKey());
        textField.setText(Strings.stringValue(value));
        context.setEditedValueProperty(textField.textProperty());
        return textField;
    }
}
