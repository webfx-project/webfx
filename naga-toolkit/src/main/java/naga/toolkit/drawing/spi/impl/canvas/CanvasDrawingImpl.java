package naga.toolkit.drawing.spi.impl.canvas;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public abstract class CanvasDrawingImpl
        <DV extends CanvasDrawableView<?, CC>, CC, CT>
        extends DrawingImpl {

    public CanvasDrawingImpl(DrawingNode drawingNode, DrawableViewFactory drawableViewFactory) {
        super(drawingNode, drawableViewFactory);
    }

    @Override
    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        super.updateDrawableParentAndChildrenViews(drawableParent);
        requestCanvasRepaint();
    }

    @Override
    protected boolean updateDrawableViewProperty(Drawable drawable, Property changedProperty) {
        boolean hitChangedProperty = super.updateDrawableViewProperty(drawable, changedProperty);
        if (hitChangedProperty || changedProperty == null)
            requestCanvasRepaint();
        return hitChangedProperty;
    }

    public void paintCanvas(CC canvasContext) {
        paintDrawables(getDrawableChildren(), canvasContext);
    }

    private void paintDrawables(Collection<Drawable> drawables, CC canvasContext) {
        CT parentTransform = getCanvasTransform(canvasContext);
        for (Drawable drawable : drawables) {
            paintDrawable(drawable, canvasContext);
            setCanvasTransform(parentTransform, canvasContext);
        }
    }

    private void paintDrawable(Drawable drawable, CC canvasContext) {
        DV drawableView = (DV) getOrCreateAndBindDrawableView(drawable);
        paintDrawableView(drawableView, canvasContext);
        if (drawable instanceof DrawableParent)
            paintDrawables(((DrawableParent) drawable).getDrawableChildren(), canvasContext);
    }

    private void paintDrawableView(DV drawableView, CC canvasContext) {
        drawableView.prepareCanvasContext(canvasContext);
        drawableView.paint(canvasContext);
    }

    public abstract void requestCanvasRepaint();

    protected abstract CT getCanvasTransform(CC canvasContext);

    protected abstract void setCanvasTransform(CT transform, CC canvasContext);

}
