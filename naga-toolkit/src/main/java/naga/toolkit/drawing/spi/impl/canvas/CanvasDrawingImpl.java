package naga.toolkit.drawing.spi.impl.canvas;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import naga.commons.util.collection.Collections;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.shapes.Point2D;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.impl.DrawingImpl;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;
import naga.toolkit.transform.Transform;

import java.util.Collection;
import java.util.List;

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
        paintDrawable(getRootDrawable(), canvasContext);
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

    public Drawable pickDrawable(Point2D point) {
        return pickFromDrawable(point, getRootDrawable());
    }

    private Drawable pickFromDrawables(Point2D point, List<Drawable> drawables) {
        // Looping in inverse order because last drawables are painted above of previous ones so they are priorities for picking
        for (int i = drawables.size() - 1; i >=0; i--) {
            Drawable pickedDrawable = pickFromDrawable(point, drawables.get(i));
            if (pickedDrawable != null)
                return pickedDrawable;
        }
        return null;
    }

    private Drawable pickFromDrawable(Point2D point, Drawable drawable) {
        // The passed point is actually expressed in the coordinates space after the transformation has been applied.
        // Before going further, we need to express it in the drawable coordinates space (ie before transformation).
        ObservableList<Transform> transforms = drawable.getTransforms();
        int n = Collections.size(transforms);
        for (int i = 0; i < n; i++)
            point = transforms.get(i).inverseTransform(point);
        // If the drawable is a parent, we return the pick result from its children
        if (drawable instanceof DrawableParent)
            return pickFromDrawables(point, ((DrawableParent) drawable).getDrawableChildren());
        // Otherwise we ask its view if it contains the point and return this drawable if this is the case
        DV drawableView = (DV) getOrCreateAndBindDrawableView(drawable);
        return drawableView.containsPoint(point) ? drawable : null;
    }


    public abstract void requestCanvasRepaint();

    protected abstract CT getCanvasTransform(CC canvasContext);

    protected abstract void setCanvasTransform(CT transform, CC canvasContext);

}
