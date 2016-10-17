package naga.toolkit.drawing.spi;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.view.ShapeView;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewFactory {

    <S extends Shape, T extends ShapeView<S>> T createShapeView(S shapeInterface);

}
