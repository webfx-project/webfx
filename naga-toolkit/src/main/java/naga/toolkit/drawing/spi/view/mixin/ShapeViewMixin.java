package naga.toolkit.drawing.spi.view.mixin;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.view.ShapeView;
import naga.toolkit.drawing.spi.view.base.ShapeViewBase;

/**
 * @author Bruno Salmon
 */
public interface ShapeViewMixin<S extends Shape, SV extends ShapeViewBase<S>> extends ShapeView<S>, DrawableViewMixin<S, SV> {
}
