package naga.toolkit.drawing.spi.impl;

import javafx.collections.ListChangeListener;
import naga.commons.util.collection.Collections;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.shapes.impl.DrawableParentImpl;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DrawingImpl extends DrawableParentImpl implements Drawing {

    private final Map<Drawable, DrawableView> drawableViews = new HashMap<>();
    private DrawableViewFactory drawableViewFactory;
    private final DrawingNotifier drawingNotifier = new DrawingNotifier() {

        @Override
        public void onDrawableParentChange(DrawableParent drawableParent) {
            syncDrawableViewsFromDrawables(drawableParent.getDrawableChildren());
            syncParentNodeFromDrawableParent(drawableParent);
        }

        @Override
        public void requestDrawableRepaint(Drawable drawable) {
            DrawingImpl.this.onDrawableRepaintRequested(drawable);
        }
    };

    protected DrawingImpl() {
        this(null);
    }

    protected DrawingImpl(DrawableViewFactory drawableViewFactory) {
        this.drawableViewFactory = drawableViewFactory;
        observeChildrenShapes(this);
    }

    public void setDrawableViewFactory(DrawableViewFactory drawableViewFactory) {
        if (this.drawableViewFactory != null) {
            Collections.forEach(drawableViews.values(), DrawableView::unbind);
            drawableViews.clear();
        }
        this.drawableViewFactory = drawableViewFactory;
    }

    private void observeChildrenShapes(DrawableParent drawableParent) {
        drawableParent.getDrawableChildren().addListener(new ListChangeListener<Drawable>() {
            @Override
            public void onChanged(Change<? extends Drawable> c) {
                drawingNotifier.onDrawableParentChange(drawableParent);
            }
        });
    }

    protected void syncParentNodeFromDrawableParent(DrawableParent drawableParent) {
    }

    protected void onDrawableRepaintRequested(Drawable shape) {
    }

    private void syncDrawableViewsFromDrawables(Collection<Drawable> drawables) {
        Collections.forEach(drawables, this::createAndBindDrawableViewAndChildren);
    }

    private void createAndBindDrawableViewAndChildren(Drawable drawable) {
        DrawableView drawableView = getOrCreateAndBindDrawableView(drawable);
        if (drawableView instanceof DrawableParent)
            syncDrawableViewsFromDrawables(((DrawableParent) drawableView).getDrawableChildren());
    }

    public DrawableView getOrCreateAndBindDrawableView(Drawable drawable) {
        DrawableView drawableView = drawableViews.get(drawable);
        if (drawableView == null) {
            drawableViews.put(drawable, drawableView = drawableViewFactory.createDrawableView(drawable));
            drawableView.bind(drawable, drawingNotifier);
            onDrawableRepaintRequested(drawable);
        }
        return drawableView;
    }
}
