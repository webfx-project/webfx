package naga.fxdata.cell.renderer;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import naga.util.Strings;
import naga.util.function.Func2;

/**
 * @author Bruno Salmon
 */
public class TextRenderer implements ValueRenderer {

    public static TextRenderer SINGLETON = new TextRenderer();

    private Func2<Object, Object, TextField> textFieldFactory = (labelKey, placeholderKey) -> new TextField();

    private TextRenderer() {}

    public void setTextFieldFactory(Func2<Object, Object, TextField> textFieldFactory) {
        this.textFieldFactory = textFieldFactory;
    }

    @Override
    public Node renderValue(Object value, ValueRenderingContext context) {
        String stringValue = Strings.toSafeString(value);
        if (context.isReadOnly())
            return new Text(stringValue);
        TextField textField = textFieldFactory.call(context.getLabelKey(), context.getPlaceholderKey());
        textField.setText(Strings.stringValue(value));
        context.setRenderedValueProperty(textField.textProperty());
        return textField;
    }
}
