package naga.providers.toolkit.javafx.drawing.view;

import naga.toolkit.drawing.shape.Shape;

/**
 * @author Bruno Salmon
 */
interface FxShapeView<N extends Shape, FxN extends javafx.scene.shape.Shape> extends FxNodeView<N, FxN>, FxLayoutMeasurable {

}
