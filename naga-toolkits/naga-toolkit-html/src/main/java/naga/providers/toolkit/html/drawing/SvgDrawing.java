package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import elemental2.Node;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.view.SvgDrawableView;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.providers.toolkit.html.util.SvgUtil;
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
    protected void createAndBindRootDrawableViewAndChildren(Drawable rootDrawable) {
        super.createAndBindRootDrawableViewAndChildren(rootDrawable);
        Node parent = (Node) drawingNode.unwrapToNativeNode();
        HtmlUtil.setChildren(parent, defsElement, getSvgDrawableElement(rootDrawable));
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        Node parent = getSvgDrawableElement(drawableParent);
        HtmlUtil.setChildren(parent, Collections.convert(drawableParent.getDrawableChildren(), this::getSvgDrawableElement));
    }

    private SvgDrawableView getOrCreateAndBindSvgDrawableView(Drawable drawable) {
        return (SvgDrawableView) getOrCreateAndBindDrawableView(drawable);
    }

    private Element getSvgDrawableElement(Drawable drawable) {
        return getOrCreateAndBindSvgDrawableView(drawable).getElement();
    }

}
