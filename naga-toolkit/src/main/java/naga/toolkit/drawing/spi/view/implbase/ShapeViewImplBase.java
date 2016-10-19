package naga.toolkit.drawing.spi.view.implbase;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.ShapeView;

/**
 * @author Bruno Salmon
 */
class ShapeViewImplBase<S extends Shape> implements ShapeView<S> {

    protected S shape;

    @Override
    public void bind(S shape, DrawingNotifier drawingNotifier) {
        this.shape = shape;
    }

    @Override
    public void unbind() {
        shape = null;
    }

    protected static void requestRepaintOnPropertyChange(DrawingNotifier drawingNotifier, Property... properties) {
        runOnPropertyChange(drawingNotifier::requestDrawingNodeRepaint, properties);
    }

    protected static void runNowAndOnPropertyChange(Runnable runnable, Property... properties) {
        runnable.run();
        runOnPropertyChange(runnable, properties);
    }

    protected static void runOnPropertyChange(Runnable runnable, Property... properties) {
        for (Property property : properties)
            property.addListener((observable, oldValue, newValue) -> runnable.run());
    }
}
