package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import elemental2.Node;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.drawing.view.SvgShapeView;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.ShapeParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;

/**
 * @author Bruno Salmon
 */
public class SvgDrawingNode extends HtmlParent</*SVGElement*/ Element> implements DrawingNode<Element>, DrawingMixin {

    private final DrawingImpl drawing = new DrawingImpl(SvgShapeViewFactory.SINGLETON) {
        @Override
        protected void syncChildrenShapesWithVisual(ShapeParent shapeParent) {
            Node parent = shapeParent == this ? node : getSvgShapeElement((Shape) shapeParent);
            HtmlUtil.setChildren(parent, Collections.convert(shapeParent.getChildrenShapes(), this::getSvgShapeElement));
        }

        private Element getSvgShapeElement(Shape shape) {
            return ((SvgShapeView) getOrCreateAndBindShapeView(shape)).getSvgShapeElement();
        }

    };

    public SvgDrawingNode() {
        this(SvgUtil.createSvgElement());
    }

    public SvgDrawingNode(Element svg) {
        super(svg);
        //svg.setAttribute("style", "width: 600; height: 250; border: 1px solid red");
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

}
