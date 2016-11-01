package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;

/**
 * @author Bruno Salmon
 */
public class SvgDrawingNode extends HtmlParent</*SVGElement*/ Element> implements DrawingNode<Element>, DrawingMixin {

    private final Drawing drawing;

    public SvgDrawingNode() {
        this(SvgUtil.createSvgElement());
    }

    public SvgDrawingNode(Element svg) {
        super(svg);
        if (!svg.hasAttribute("width"))
            svg.setAttribute("width", "100%");
         drawing = new SvgDrawing(this);
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

}
