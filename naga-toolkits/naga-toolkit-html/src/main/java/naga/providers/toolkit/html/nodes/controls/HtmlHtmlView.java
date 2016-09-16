package naga.providers.toolkit.html.nodes.controls;

import elemental2.HTMLElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.nodes.HtmlNode;
import naga.toolkit.spi.nodes.controls.HtmlView;

/**
 * @author Bruno Salmon
 */
public class HtmlHtmlView extends HtmlNode<HTMLElement> implements HtmlView<HTMLElement> {

    public HtmlHtmlView() {
        this(HtmlUtil.createDivElement());
    }

    public HtmlHtmlView(HTMLElement text) {
        super(text);
        textProperty.addListener((observable, oldValue, newValue) -> node.innerHTML = newValue);
    }

    private final Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

}
