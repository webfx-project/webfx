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
import naga.toolkit.drawing.spi.view.DrawableView;

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
        Node parent = drawingNode.unwrapToNativeNode();
        HtmlUtil.setChildren(parent, defsElement, getSvgDrawableElement(rootDrawable));
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        Node parent = getSvgDrawableElement(drawableParent);
        HtmlUtil.setChildren(parent, Collections.convert(drawableParent.getDrawableChildren(), this::getSvgDrawableElement));
    }

    private SvgDrawableView getOrCreateAndBindSvgDrawableView(Drawable drawable) {
        DrawableView drawableView = getOrCreateAndBindDrawableView(drawable); // Should be a FxDrawableView (but may be UnimplementedDrawableView if no view factory is registered for this drawable)
        if (drawableView instanceof SvgDrawableView) // Should be a SvgDrawableView
            return (SvgDrawableView) drawableView;
        // Shouldn't happen unless no view factory is registered for this drawable (probably UnimplementedDrawableView was returned)
        return null; // returning null in this case to indicate there is no view to show
    }

    private Element getSvgDrawableElement(Drawable drawable) {
        SvgDrawableView svgDrawableView = getOrCreateAndBindSvgDrawableView(drawable);
        return svgDrawableView == null ? null : svgDrawableView.getElement();
    }

}
