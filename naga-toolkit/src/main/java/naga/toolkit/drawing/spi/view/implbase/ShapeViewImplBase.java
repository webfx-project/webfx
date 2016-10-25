package naga.toolkit.drawing.spi.view.implbase;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.ShapeView;

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
                shape.strokeProperty(),
                shape.strokeWidthProperty(),
                shape.strokeLineCapProperty(),
                shape.strokeLineJoinProperty(),
                shape.strokeMiterLimitProperty());
    }

    @Override
    public void unbind() {
        shape = null;
    }

    protected static void requestRepaintShapeOnPropertiesChange(DrawingNotifier drawingNotifier, Shape shape, Property... properties) {
        runOnPropertiesChange(() -> drawingNotifier.requestShapeRepaint(shape), properties);
    }

    protected static void runNowAndOnPropertiesChange(Runnable runnable, Property... properties) {
        runnable.run();
        runOnPropertiesChange(runnable, properties);
    }

    protected static void runOnPropertiesChange(Runnable runnable, Property... properties) {
        for (Property property : properties)
            property.addListener((observable, oldValue, newValue) -> runnable.run());
    }
}
