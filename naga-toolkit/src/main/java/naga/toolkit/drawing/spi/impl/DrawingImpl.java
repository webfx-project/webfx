package naga.toolkit.drawing.spi.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.ShapeParent;
import naga.toolkit.drawing.spi.Drawing;
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

    public DrawingImpl() {
    }

    public DrawingImpl(ShapeViewFactory shapeViewFactory) {
        this.shapeViewFactory = shapeViewFactory;
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

    public void draw() {
        drawShapes(children);
    }

    private void drawShapes(Collection<Shape> shapes) {
        for (Shape shape : shapes)
            drawShape(shape);
    }

    private void drawShape(Shape shape) {
        ShapeView shapeView = getOrCreateShapeView(shape);
        if (shapeView instanceof ShapeParent)
            drawShapes(((ShapeParent) shapeView).getChildrenShapes());
    }

    public ShapeView getOrCreateShapeView(Shape shape) {
        ShapeView shapeView = shapeViews.get(shape);
        if (shapeView == null) {
            shapeViews.put(shape, shapeView = shapeViewFactory.createShapeView(shape));
            shapeView.bind(shape);
        }
        return shapeView;
    }
}
