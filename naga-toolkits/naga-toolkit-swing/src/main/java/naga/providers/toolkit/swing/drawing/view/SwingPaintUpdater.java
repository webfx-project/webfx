package naga.providers.toolkit.swing.drawing.view;

import javafx.beans.property.Property;
import naga.providers.toolkit.swing.util.SwingPaints;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.Shape;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingPaintUpdater {

    Paint swingPaint;
    private LinearGradient linearGradient;

    protected boolean updateFromShape(Shape shape, Property changedProperty) {
        boolean hitChangedProperty = false;
        if (changedProperty == null || (hitChangedProperty = changedProperty == shape.fillProperty()))
            updateFromPaint(shape.getFill());
        return hitChangedProperty;
    }

    void updateFromPaint(naga.toolkit.drawing.paint.Paint paint) {
        linearGradient = paint instanceof LinearGradient ? (LinearGradient) paint : null;
        swingPaint = isProportionalGradient() ? null : SwingPaints.toSwingPaint(paint);
    }

    private boolean isProportionalGradient() {
        return linearGradient != null && linearGradient.isProportional();
    }

    void updateProportionalGradient(Double width, Double height) {
        if (width != null && height != null && isProportionalGradient())
            swingPaint = SwingPaints.toSwingLinearGradient(linearGradient, width.floatValue(), height.floatValue());
    }

}
