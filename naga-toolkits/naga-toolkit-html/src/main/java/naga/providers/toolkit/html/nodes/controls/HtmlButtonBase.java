package naga.providers.toolkit.html.nodes.controls;

import elemental2.Element;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.nodes.controls.ButtonBase;

/**
 * @author Bruno Salmon
 */
public class HtmlButtonBase<N extends Element> extends HtmlNode<N> implements ButtonBase<N> {

    public HtmlButtonBase(N button) {
        super(button);
        textProperty.setValue(button.textContent);
        textProperty.addListener((observable, oldValue, newValue) -> button.textContent = newValue);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }
}
