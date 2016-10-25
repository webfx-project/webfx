package naga.toolkit.drawing.spi.impl;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.ShapeParent;
import naga.toolkit.drawing.spi.Drawing;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.ShapeViewFactory;
import naga.toolkit.drawing.spi.view.ShapeView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class DrawingImpl implements Drawing {

    private final Map<Shape, ShapeView> shapeViews = new HashMap<>();
    private ShapeViewFactory shapeViewFactory;
    private final DrawingNotifier drawingNotifier = new DrawingNotifier() {

        @Override
        public void onChildrenShapesListChange(ShapeParent shapeParent) {
            syncShapeViewListFromShapeList(shapeParent.getChildrenShapes());
            syncNodeListFromShapeViewList(shapeParent);
        }

        @Override
        public void requestShapeRepaint(Shape shape) {
            DrawingImpl.this.onShapeRepaintRequested(shape);
        }
    };

    protected DrawingImpl() {
        this(null);
    }

    protected DrawingImpl(ShapeViewFactory shapeViewFactory) {
        this.shapeViewFactory = shapeViewFactory;
        observeChildrenShapes(this);
    }

    private final ObservableList<Shape> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<Shape> getChildrenShapes() {
        return children;
    }

    public void setShapeViewFactory(ShapeViewFactory shapeViewFactory) {
        if (this.shapeViewFactory != null) {
            shapeViews.values().forEach(ShapeView::unbind);
            shapeViews.clear();
        }
        this.shapeViewFactory = shapeViewFactory;
    }

    private void observeChildrenShapes(ShapeParent shapeParent) {
        shapeParent.getChildrenShapes().addListener(new ListChangeListener<Shape>() {
            @Override
            public void onChanged(Change<? extends Shape> c) {
                drawingNotifier.onChildrenShapesListChange(shapeParent);
            }
        });
    }

    protected void syncNodeListFromShapeViewList(ShapeParent shapeParent) {
    }

    protected void onShapeRepaintRequested(Shape shape) {
    }

    private void syncShapeViewListFromShapeList(Collection<Shape> shapes) {
        shapes.forEach(this::createAndBindShapeViewAndChildren);
    }

    private void createAndBindShapeViewAndChildren(Shape shape) {
        ShapeView shapeView = getOrCreateAndBindShapeView(shape);
        if (shapeView instanceof ShapeParent)
            syncShapeViewListFromShapeList(((ShapeParent) shapeView).getChildrenShapes());
    }

    public ShapeView getOrCreateAndBindShapeView(Shape shape) {
        ShapeView shapeView = shapeViews.get(shape);
        if (shapeView == null) {
            shapeViews.put(shape, shapeView = shapeViewFactory.createShapeView(shape));
            shapeView.bind(shape, drawingNotifier);
            onShapeRepaintRequested(shape);
        }
        return shapeView;
    }
}
