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
    private final Runnable drawingNodeRepaintRequester;
    private final DrawingNotifier drawingNotifier = new DrawingNotifier() {

        @Override
        public void onChildrenShapesChange(ShapeParent shapeParent) {
            createAndBindShapeViews(shapeParent.getChildrenShapes());
            syncChildrenShapesWithVisual(shapeParent);
        }

        @Override
        public void requestDrawingNodeRepaint() {
            if (drawingNodeRepaintRequester != null)
                drawingNodeRepaintRequester.run();
        }
    };

    public DrawingImpl() {
        this(null, null);
    }

    public DrawingImpl(ShapeViewFactory shapeViewFactory) {
        this(shapeViewFactory, null);
    }

    public DrawingImpl(ShapeViewFactory shapeViewFactory, Runnable drawingNodeRepaintRequester) {
        this.shapeViewFactory = shapeViewFactory;
        this.drawingNodeRepaintRequester = drawingNodeRepaintRequester;
        observeChildrenShapes(this);
    }

    private final ObservableList<Shape> children = FXCollections.observableArrayList();
    @Override
    public ObservableList<Shape> getChildrenShapes() {
        return children;
    }

    public void setShapeViewFactory(ShapeViewFactory shapeViewFactory) {
        if (this.shapeViewFactory != null) {
            for (ShapeView shapeView : shapeViews.values())
                shapeView.unbind();
            shapeViews.clear();
        }
        this.shapeViewFactory = shapeViewFactory;
    }

    private void observeChildrenShapes(ShapeParent shapeParent) {
        shapeParent.getChildrenShapes().addListener(new ListChangeListener<Shape>() {
            @Override
            public void onChanged(Change<? extends Shape> c) {
                drawingNotifier.onChildrenShapesChange(shapeParent);
            }
        });
    }

    protected void syncChildrenShapesWithVisual(ShapeParent shapeParent) {
    }

    private void createAndBindShapeViews(Collection<Shape> shapes) {
        for (Shape shape : shapes)
            createAndBindShapeViewAndChildren(shape);
    }

    private void createAndBindShapeViewAndChildren(Shape shape) {
        ShapeView shapeView = getOrCreateAndBindShapeView(shape);
        if (shapeView instanceof ShapeParent)
            createAndBindShapeViews(((ShapeParent) shapeView).getChildrenShapes());
    }

    public ShapeView getOrCreateAndBindShapeView(Shape shape) {
        ShapeView shapeView = shapeViews.get(shape);
        if (shapeView == null) {
            shapeViews.put(shape, shapeView = shapeViewFactory.createShapeView(shape));
            shapeView.bind(shape, drawingNotifier);
        }
        return shapeView;
    }
}
