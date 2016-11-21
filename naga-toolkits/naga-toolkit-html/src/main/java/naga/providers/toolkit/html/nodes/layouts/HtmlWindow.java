package naga.providers.toolkit.html.nodes.layouts;

import elemental2.Node;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

import static elemental2.Global.document;

/**
 * @author Bruno Salmon
 */
public class HtmlWindow implements Window {

    public HtmlWindow() {
        nodeProperty.addListener((observable, oldValue, newValue) -> { if (newValue != null) setWindowContent(newValue.unwrapToNativeNode()); });
        titleProperty().addListener((observable, oldValue, newValue) -> document.title = newValue);
    }

    private void setWindowContent(Node content) {
        //Platform.log("Setting window root " + content);
        HtmlUtil.setBodyContent(content);
        //Platform.log("Ok");
    }

    private final Property<GuiNode> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
