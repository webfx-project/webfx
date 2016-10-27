package naga.toolkit.drawing.spi.view.implbase;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.ShapeView;
import naga.toolkit.properties.util.Properties;

/**
 * @author Bruno Salmon
 */
abstract class ShapeViewImplBase<S extends Shape> implements ShapeView<S> {

    protected S shape;

    @Override
    public void bind(S shape, DrawingNotifier drawingNotifier) {
        this.shape = shape;
        requestRepaintShapeOnPropertiesChange(drawingNotifier, shape,
                shape.fillProperty(),
                shape.smoothProperty(),
                shape.strokeProperty(),
                shape.strokeWidthProperty(),
                shape.strokeLineCapProperty(),
                shape.strokeLineJoinProperty(),
                shape.strokeMiterLimitProperty(),
                shape.strokeDashOffsetProperty());
    }

    @Override
    public void unbind() {
        shape = null;
    }

    protected static void requestRepaintShapeOnPropertiesChange(DrawingNotifier drawingNotifier, Shape shape, Property... properties) {
        Properties.runOnPropertiesChange(() -> drawingNotifier.requestShapeRepaint(shape), properties);
    }

}
