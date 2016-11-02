package naga.toolkit.drawing.spi.view.mixin;

import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.view.TextShapeView;
import naga.toolkit.drawing.spi.view.base.TextShapeViewBase;

/**
 * @author Bruno Salmon
 */
public interface TextShapeViewMixin extends TextShapeView, ShapeViewMixin<TextShape, TextShapeViewBase> {
}
