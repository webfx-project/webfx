package naga.providers.toolkit.html.fx.html;

import elemental2.HTMLElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.commons.util.Numbers;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.fx.spi.Drawing;
import naga.toolkit.fx.spi.DrawingMixin;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public class HtmlDrawingNode extends HtmlParent<HTMLElement> implements DrawingNode, DrawingMixin {

    private final Drawing drawing;

    public HtmlDrawingNode() {
        this(HtmlUtil.createDivElement());
    }

    private HtmlDrawingNode(HTMLElement container) {
        super(container);
        //HtmlUtil.setStyleAttribute(container, "clip-path", "inset(0 0% 0% 0)"); // Doesn't seem to work with absolute position
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        drawing = new HtmlDrawing(this)/* {
            @Override
            protected void pulse() {
                updateWidthProperty(container);
                super.pulse();
            }
        }*/;
        Properties.runNowAndOnPropertiesChange(property -> updateContainerWidth(container), widthProperty());
        Properties.runNowAndOnPropertiesChange(property -> updateContainerHeight(container), heightProperty());
    }

    private void updateWidthProperty(HTMLElement container) {
        setWidth(container.clientWidth);
    }

    private void updateContainerWidth(HTMLElement container) {
        double width = Numbers.doubleValue(getWidth());
        HtmlUtil.setStyleAttribute(container, "width",
                (width > 0 ?
                        width :
                        getDrawing().getRootNode() != null ?
                                getDrawing().getRootNode().prefWidth(-1) :
                                0)
                        + "px");
    }

    private void updateContainerHeight(HTMLElement container) {
        double height = Numbers.doubleValue(getHeight());
        HtmlUtil.setStyleAttribute(container, "height",
                (height > 0 ?
                        height :
                        getDrawing().getRootNode() != null ?
                                getDrawing().getRootNode().prefHeight(-1) :
                                0)
                        + "px");
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);

    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);

    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

}
