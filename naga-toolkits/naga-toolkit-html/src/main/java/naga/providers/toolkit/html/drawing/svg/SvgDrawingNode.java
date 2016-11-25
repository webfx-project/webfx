package naga.providers.toolkit.html.drawing.svg;

import elemental2.Element;
import elemental2.EventTarget;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class SvgDrawingNode extends HtmlParent</*SVGElement*/ Element> implements DrawingNode, DrawingMixin {

    private final Drawing drawing;

    public SvgDrawingNode() {
        this(SvgUtil.createSvgElement());
    }

    public SvgDrawingNode(Element svg) {
        super(svg);
        if (!svg.hasAttribute("width"))
            svg.setAttribute("width", "100%");
        if (!svg.hasAttribute("height"))
            svg.setAttribute("height", "600px");
        heightProperty().addListener((observable, oldValue, newHeight) -> svg.setAttribute("height", "" + newHeight + "px"));
        drawing = new SvgDrawing(this);
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
