package naga.toolkit.drawing.spi.view.implbase;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
abstract class ShapeViewImplBase<S extends Shape> extends DrawableViewImplBase<S> implements DrawableView<S> {

    @Override
    public void bind(S shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        requestRepaintDrawableOnPropertiesChange(drawingNotifier, shape,
                shape.fillProperty(),
                shape.smoothProperty(),
                shape.strokeProperty(),
                shape.strokeWidthProperty(),
                shape.strokeLineCapProperty(),
                shape.strokeLineJoinProperty(),
                shape.strokeMiterLimitProperty(),
                shape.strokeDashOffsetProperty());
    }

}
