package naga.toolkit.drawing.spi.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import naga.commons.util.collection.Collections;
import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingNode;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;
import naga.toolkit.util.ObservableLists;
import naga.toolkit.util.Properties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public abstract class DrawingImpl implements Drawing {

    protected final DrawingNode drawingNode;
    private DrawableViewFactory drawableViewFactory;
    private final Map<Drawable, DrawableView> drawableViews = new HashMap<>();
    private final Property<Drawable> rootDrawableProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Drawable> rootDrawableProperty() {
        return rootDrawableProperty;
    }

    private final DrawingRequester drawingRequester = new DrawingRequester() {

        @Override
        public void requestDrawableParentAndChildrenViewsUpdate(DrawableParent drawableParent) {
            drawingThreadLocal.set(DrawingImpl.this);
            updateDrawableParentAndChildrenViews(drawableParent);
            drawingThreadLocal.set(null);
        }

        @Override
        public void requestDrawableViewUpdateProperty(Drawable drawable, Property changedProperty) {
            drawingThreadLocal.set(DrawingImpl.this);
            updateDrawableViewProperty(drawable, changedProperty);
            drawingThreadLocal.set(null);
        }

        @Override
        public void requestDrawableViewUpdateList(Drawable drawable, ObservableList changedList) {
            drawingThreadLocal.set(DrawingImpl.this);
            updateDrawableViewList(drawable, changedList);
            drawingThreadLocal.set(null);
        }
    };

    protected DrawingImpl(DrawingNode drawingNode, DrawableViewFactory drawableViewFactory) {
        this.drawingNode = drawingNode;
        this.drawableViewFactory = drawableViewFactory;
        Properties.runOnPropertiesChange(drawableProperty -> createAndBindRootDrawableViewAndChildren(getRootDrawable()), rootDrawableProperty());
    }

    private final static ThreadLocal<DrawingImpl> drawingThreadLocal = new ThreadLocal<>();
    public static DrawingImpl getThreadLocalDrawing() {
        return drawingThreadLocal.get();
    }

    public void setDrawableViewFactory(DrawableViewFactory drawableViewFactory) {
        if (this.drawableViewFactory != null) {
            Collections.forEach(drawableViews.values(), DrawableView::unbind);
            drawableViews.clear();
        }
        this.drawableViewFactory = drawableViewFactory;
    }

    protected void keepDrawableParentAndChildrenViewsUpdated(DrawableParent drawableParent) {
        ObservableLists.runNowAndOnListChange(() -> updateDrawableParentAndChildrenViews(drawableParent), drawableParent.getDrawableChildren());
    }

    protected void updateDrawableParentAndChildrenViews(DrawableParent drawableParent) {
        updateDrawableChildrenViews(drawableParent.getDrawableChildren());
    }

    protected boolean updateDrawableViewProperty(Drawable drawable, Property changedProperty) {
        return updateDrawableViewProperty(getOrCreateAndBindDrawableView(drawable), changedProperty);
    }

    private boolean updateDrawableViewProperty(DrawableView drawableView, Property changedProperty) {
        return drawableView.updateProperty(changedProperty);
    }

    private boolean updateDrawableViewList(Drawable drawable, ObservableList changedList) {
        return updateDrawableViewList(getOrCreateAndBindDrawableView(drawable), changedList);
    }

    private boolean updateDrawableViewList(DrawableView drawableView, ObservableList changedList) {
        return drawableView.updateList(changedList);
    }

    private void updateDrawableChildrenViews(Collection<Drawable> drawables) {
        Collections.forEach(drawables, this::createAndBindDrawableViewAndChildren);
    }

    protected void createAndBindRootDrawableViewAndChildren(Drawable rootDrawable) {
        drawingThreadLocal.set(DrawingImpl.this);
        createAndBindDrawableViewAndChildren(rootDrawable);
        drawingThreadLocal.set(null);
    }

    private void createAndBindDrawableViewAndChildren(Drawable drawable) {
        DrawableView drawableView = getOrCreateAndBindDrawableView(drawable);
        if (drawableView instanceof DrawableParent)
            updateDrawableChildrenViews(((DrawableParent) drawableView).getDrawableChildren());
    }

    public DrawableView getOrCreateAndBindDrawableView(Drawable drawable) {
        DrawableView drawableView = drawableViews.get(drawable);
        if (drawableView == null) {
            drawableViews.put(drawable, drawableView = drawableViewFactory.createDrawableView(drawable));
            drawableView.bind(drawable, drawingRequester);
            if (drawable instanceof DrawableParent)
                keepDrawableParentAndChildrenViewsUpdated((DrawableParent) drawable);
        }
        return drawableView;
    }

    protected boolean isRootDrawable(Drawable drawable) {
        return drawable == getRootDrawable();
    }
}
