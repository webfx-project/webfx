package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import elemental2.Node;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.view.SvgDrawableView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.impl.DrawingImpl;

/**
 * @author Bruno Salmon
 */
public class SvgDrawing extends DrawingImpl {

    private final Element defsElement = SvgUtil.createSvgDefs();

    SvgDrawing(SvgDrawingNode svgDrawingNode) {
        super(svgDrawingNode, SvgDrawableViewFactory.SINGLETON);
    }

    public Element addDef(Element def) {
        defsElement.appendChild(def);
        return def;
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        boolean isRoot = isDrawableParentRoot(drawableParent);
        Node parent = isRoot ? (Node) drawingNode.unwrapToNativeNode() : getSvgDrawableElement((Drawable) drawableParent);
        HtmlUtil.setChildren(parent, Collections.convert(drawableParent.getDrawableChildren(), this::getSvgDrawableElement));
        if (isRoot)
            HtmlUtil.appendFirstChild(parent, defsElement);
    }

    private SvgDrawableView getOrCreateAndBindSvgDrawableView(Drawable drawable) {
        return (SvgDrawableView) getOrCreateAndBindDrawableView(drawable);
    }

    private Element getSvgDrawableElement(Drawable drawable) {
        return getOrCreateAndBindSvgDrawableView(drawable).getElement();
    }

}
