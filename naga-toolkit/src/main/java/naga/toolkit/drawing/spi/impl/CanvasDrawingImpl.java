package naga.toolkit.drawing.spi.impl;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class CanvasDrawingImpl<DV extends DrawableView, CC, CT> extends DrawingImpl {

    public CanvasDrawingImpl(DrawingNode drawingNode, DrawableViewFactory drawableViewFactory) {
        super(drawingNode, drawableViewFactory);
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        super.updateDrawableParentAndChildrenViews(drawableParent);
        requestCanvasRepaint();
    }

    @Override
    protected void updateDrawableView(Drawable drawable, Property changedProperty) {
        updateDrawableView((DV) getOrCreateAndBindDrawableView(drawable), changedProperty);
        requestCanvasRepaint();
    }

    protected abstract void updateDrawableView(DV drawableView, Property changedProperty);

    protected abstract void requestCanvasRepaint();

    public void paintCanvas(CC canvasContext) {
        paintDrawables(getDrawableChildren(), canvasContext);
    }

    private void paintDrawables(Collection<Drawable> drawables, CC canvasContext) {
        CT parentTransform = getCanvasTransform(canvasContext);
        for (Drawable drawable : drawables) {
            setCanvasTransform(parentTransform, canvasContext);
            paintDrawable(drawable, canvasContext);
        }
    }

    private void paintDrawable(Drawable drawable, CC canvasContext) {
        DV drawableView = (DV) getOrCreateAndBindDrawableView(drawable);
        paintDrawableView(drawableView, canvasContext);
        if (drawable instanceof DrawableParent)
            paintDrawables(((DrawableParent) drawable).getDrawableChildren(), canvasContext);
    }

    protected abstract CT getCanvasTransform(CC canvasContext);

    protected abstract void setCanvasTransform(CT transform, CC canvasContext);

    protected abstract void paintDrawableView(DV drawableView, CC canvasContext);

}
