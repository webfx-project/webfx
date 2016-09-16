package naga.providers.toolkit.javafx.nodes.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.web.WebView;
import naga.providers.toolkit.javafx.nodes.FxNode;
import naga.toolkit.spi.nodes.controls.HtmlView;

/**
 * @author Bruno Salmon
 */
public class FxHtmlView extends FxNode<WebView> implements HtmlView<WebView> {

    public FxHtmlView() {
        this(createWebView());
    }

    public FxHtmlView(WebView text) {
        super(text);
        textProperty.addListener((observable, oldValue, newValue) -> node.getEngine().loadContent(newValue));
    }

    private static WebView createWebView() {
        return new WebView();
    }

    private Property<String> textProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> textProperty() {
        return textProperty;
    }

}
