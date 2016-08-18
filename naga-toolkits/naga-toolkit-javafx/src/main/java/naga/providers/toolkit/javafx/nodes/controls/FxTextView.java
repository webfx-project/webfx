package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.scene.text.Text;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.spi.nodes.controls.TextView;

/**
 * @author Bruno Salmon
 */
public class FxTextView extends FxNode<Text> implements TextView<Text> {

    public FxTextView() {
        this(createText());
    }

    public FxTextView(Text text) {
        super(text);
    }

    private static Text createText() {
        return new Text();
    }

    @Override
    public Property<String> textProperty() {
        return node.textProperty();
    }

}
