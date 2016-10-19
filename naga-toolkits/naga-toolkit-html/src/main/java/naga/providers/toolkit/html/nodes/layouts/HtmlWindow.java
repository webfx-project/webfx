package naga.providers.toolkit.html.nodes.layouts;

import elemental2.Element;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

import static elemental2.Global.document;

/**
 * @author Bruno Salmon
 */
public class HtmlWindow implements Window<Element> {

    public HtmlWindow() {
        nodeProperty.addListener((observable, oldValue, newValue) -> { if (newValue != null) setWindowContent(newValue.unwrapToNativeNode()); });
        titleProperty().addListener((observable, oldValue, newValue) -> document.title = newValue);
    }

    private void setWindowContent(Element content) {
        //Platform.log("Setting window root " + content);
        HtmlUtil.setChild(document.body, content);
        //Platform.log("Ok");
    }

    private final Property<GuiNode<Element>> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Element>> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
