package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import elemental2.Node;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.view.SvgDrawableView;
import naga.providers.toolkit.html.nodes.HtmlParent;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingMixin;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;

/**
 * @author Bruno Salmon
 */
public class SvgDrawingNode extends HtmlParent</*SVGElement*/ Element> implements DrawingNode<Element>, DrawingMixin {

    private final Element defsElement = SvgUtil.createSvgDefs();

    private final Drawing drawing = new DrawingImpl(SvgDrawableViewFactory.SINGLETON) {
        @Override
        protected void syncParentNodeFromDrawableParent(DrawableParent drawableParent) {
            boolean isRoot = drawableParent == this;
            Node parent = isRoot ? node : getSvgDrawableElement((Drawable) drawableParent);
            HtmlUtil.setChildren(parent, Collections.convert(drawableParent.getDrawableChildren(), this::getSvgDrawableElement));
            if (isRoot)
                HtmlUtil.appendFirstChild(node, defsElement);
        }

        private SvgDrawableView getOrCreateAndBindSvgDrawableView(Drawable drawable) {
            return (SvgDrawableView) getOrCreateAndBindDrawableView(drawable);
        }

        private Element getSvgDrawableElement(Drawable drawable) {
            return getOrCreateAndBindSvgDrawableView(drawable).getSvgDrawableElement();
        }

        @Override
        protected void onDrawableRepaintRequested(Drawable drawable) {
            getOrCreateAndBindSvgDrawableView(drawable).syncSvgPropertiesFromDrawable(SvgDrawingNode.this);
        }
    };

    public SvgDrawingNode() {
        this(SvgUtil.createSvgElement());
    }

    public SvgDrawingNode(Element svg) {
        super(svg);
        if (!svg.hasAttribute("width"))
            svg.setAttribute("width", "100%");
    }

    public Element addDef(Element def) {
        defsElement.appendChild(def);
        return def;
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

}
