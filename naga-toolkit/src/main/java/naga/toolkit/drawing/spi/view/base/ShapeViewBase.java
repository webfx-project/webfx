package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.DrawableView;

/**
 * @author Bruno Salmon
 */
public abstract class ShapeViewBase<S extends Shape> extends DrawableViewBase<S> implements DrawableView<S> {

    @Override
    public void bind(S shape, DrawingRequester drawingRequester) {
        super.bind(shape, drawingRequester);
        requestDrawableViewUpdateOnPropertiesChange(shape, drawingRequester,
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
