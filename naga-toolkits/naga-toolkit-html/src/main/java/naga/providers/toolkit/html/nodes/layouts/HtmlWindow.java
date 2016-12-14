package naga.providers.toolkit.html.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.fx.html.HtmlDrawingNode;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.spi.nodes.layouts.Window;

import static elemental2.Global.document;
import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class HtmlWindow implements Window {

    public HtmlWindow() {
        nodeProperty.addListener((observable, oldValue, newValue) -> setWindowContent(newValue));
        titleProperty().addListener((observable, oldValue, newValue) -> document.title = newValue);
    }

    private void setWindowContent(Node node) {
        DrawingNode drawingNode = naga.toolkit.spi.Toolkit.get().createDrawingNode();
        drawingNode.setRootNode(node);
        setWindowContent(((HtmlDrawingNode) drawingNode).unwrapToNativeNode());
        document.body.style.overflow = "hidden";
        drawingNode.setWidth(window.innerWidth);
        drawingNode.setHeight(window.innerHeight);
        window.onresize = a -> {
            drawingNode.setWidth(window.innerWidth);
            drawingNode.setHeight(window.innerHeight);
            return null;
        };
    }

    private void setWindowContent(elemental2.Node content) {
        //Platform.log("Setting window root " + content);
        HtmlUtil.setBodyContent(content);
        //Platform.log("Ok");
    }

    private final Property<Node> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }
}
