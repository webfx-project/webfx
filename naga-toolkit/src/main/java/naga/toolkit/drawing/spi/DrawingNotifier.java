package naga.toolkit.drawing.spi;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.ShapeParent;

/**
 * @author Bruno Salmon
 */
public interface DrawingNotifier {

    void onChildrenShapesListChange(ShapeParent shapeParent);

    void requestShapeRepaint(Shape shape);

}
