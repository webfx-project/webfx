package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.nodes.controls.TextView;

/**
 * @author Bruno Salmon
 */
public class HtmlTextView extends HtmlNode<HTMLElement> implements TextView {

    public HtmlTextView() {
        this(HtmlUtil.createSpanElement());
    }

    public HtmlTextView(HTMLElement text) {
        super(text);
        textProperty.addListener((observable, oldValue, newValue) -> node.textContent = newValue);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

}
