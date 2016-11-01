package naga.toolkit.drawing.spi.view.implbase;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
abstract class ShapeViewImplBase<S extends Shape> extends DrawableViewImplBase<S> implements DrawableView<S> {

    @Override
    public void bind(S shape, DrawingRequester drawingRequester) {
        super.bind(shape, drawingRequester);
        requestDrawableViewUpdateOnPropertiesChange(drawingRequester, shape,
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
