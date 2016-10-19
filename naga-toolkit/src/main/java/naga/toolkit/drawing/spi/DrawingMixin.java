package naga.toolkit.drawing.spi;

import javafx.collections.ObservableList;
import naga.toolkit.drawing.shapes.Shape;

/**
 * @author Bruno Salmon
 */
public interface DrawingMixin extends Drawing {

    Drawing getDrawing();

    @Override
    default void setShapeViewFactory(ShapeViewFactory shapeViewFactory) {
        getDrawing().setShapeViewFactory(shapeViewFactory);
    }

    @Override
    default ObservableList<Shape> getChildrenShapes() {
        return getDrawing().getChildrenShapes();
    }
}
