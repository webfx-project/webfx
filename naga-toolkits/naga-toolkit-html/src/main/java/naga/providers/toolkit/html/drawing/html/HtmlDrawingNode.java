package naga.providers.toolkit.html.drawing.html;

import elemental2.Element;
import elemental2.EventTarget;
import elemental2.HTMLElement;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class HtmlDrawingNode extends HtmlParent<HTMLElement> implements DrawingNode, DrawingMixin {

    private final Drawing drawing;

    public HtmlDrawingNode() {
        this(HtmlUtil.createDivElement());
    }

    public HtmlDrawingNode(HTMLElement container) {
        super(container);
        HtmlUtil.setStyleAttribute(container, "clip-path", "inset(0 0% 0% 0)");
        HtmlUtil.setStyleAttribute(container, "width", "100%");
        HtmlUtil.setStyleAttribute(container, "height", "600px");
        heightProperty().addListener((observable, oldValue, newHeight) -> HtmlUtil.setStyleAttribute(container, "height", "" + newHeight + "px"));
        drawing = new HtmlDrawing(this);
        HtmlUtil.runOnAttached(node, () -> {
            updateWidthProperty();
            window.addEventListener("resize", (EventTarget.AddEventListenerListenerCallback) a -> {
                updateWidthProperty();
                return true;
            }, false);
        });
    }

    private void updateWidthProperty() {
        if (node.parentNode instanceof Element)
            setWidth(getElementWidth((Element) node.parentNode));
    }

    private native double getElementWidth(Element e) /*-{
        var style = $wnd.getComputedStyle(e);
        return e.clientWidth - parseFloat(style.paddingLeft) - parseFloat(style.paddingRight);
    }-*/;

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
