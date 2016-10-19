package naga.toolkit.drawing.spi;

import naga.toolkit.drawing.shapes.ShapeParent;

/**
 * @author Bruno Salmon
 */
public interface DrawingNotifier {

    void onChildrenShapesChange(ShapeParent shapeParent);

    void requestDrawingNodeRepaint();

}
