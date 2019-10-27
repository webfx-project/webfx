package webfx.extras.cell.renderer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import webfx.platform.shared.util.Strings;

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
            return applyRenderingContextToText(new Text(stringValue), context);
        TextField textField = applyRenderingContextToTextField(textFieldFactory.apply(context.getLabelKey(), context.getPlaceholderKey()), context);
        context.bindEditedValuePropertyTo(textField.textProperty(), stringValue);
        return textField;
    }

    private static Text applyRenderingContextToText(Text text, ValueRenderingContext context) {
        if (context.getTextAlign() != null) {
            TextAlignment textAlignment = null;
            switch (context.getTextAlign()) {
                case "left":   textAlignment = TextAlignment.LEFT; break;
                case "center": textAlignment = TextAlignment.CENTER; break;
                case "right":  textAlignment = TextAlignment.RIGHT; break;
            }
            text.setTextAlignment(textAlignment);
        }
        return text;
    }

    private static TextField applyRenderingContextToTextField(TextField textField, ValueRenderingContext context) {
        if (context.getTextAlign() != null) {
            Pos textAlignment = null;
            switch (context.getTextAlign()) {
                case "left":   textAlignment = Pos.BASELINE_LEFT; break;
                case "center": textAlignment = Pos.BASELINE_CENTER; break;
                case "right":  textAlignment = Pos.BASELINE_RIGHT; break;
            }
            textField.setAlignment(textAlignment);
        }
        return textField;
    }
}
