package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import javafx.collections.ListChangeListener;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.HtmlUtil;
import naga.providers.toolkit.html.drawing.view.SvgShapeView;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;

/**
 * @author Bruno Salmon
 */
public class SvgDrawingNode extends HtmlParent</*SVGElement*/ Element> implements DrawingNode<Element>, DrawingMixin {

    private final DrawingImpl drawing = new DrawingImpl(SvgShapeViewFactory.SINGLETON);

    public SvgDrawingNode() {
        this(SvgUtil.createSvgElement());
    }

    public SvgDrawingNode(Element svg) {
        super(svg);
        drawing.getChildrenShapes().addListener(new ListChangeListener<Shape>() {
            @Override
            public void onChanged(Change<? extends Shape> c) {
                drawing.draw();
                HtmlUtil.setChildren(svg, Collections.convert(drawing.getChildrenShapes(), shape -> ((SvgShapeView) drawing.getOrCreateShapeView(shape)).getSvgShapeElement()));
            }
        });
        //svg.setAttribute("style", "width: 600; height: 250; border: 1px solid red");
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

}
