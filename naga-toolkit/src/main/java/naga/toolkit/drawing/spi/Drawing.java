package naga.toolkit.drawing.spi;

import naga.toolkit.drawing.shapes.ShapeParent;

/**
 * @author Bruno Salmon
 */
public interface Drawing extends ShapeParent {

    void setShapeViewFactory(ShapeViewFactory shapeViewFactory);

}
