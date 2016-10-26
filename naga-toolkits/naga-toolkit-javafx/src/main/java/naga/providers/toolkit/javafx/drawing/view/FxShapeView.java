package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.view.ShapeView;

/**
 * @author Bruno Salmon
 */
public interface FxShapeView<S extends Shape, F extends javafx.scene.shape.Shape> extends ShapeView<S> {

    F getFxShape();

}
