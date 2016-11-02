package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingPaints;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.Shape;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingPaintUpdater {

    private Paint swingPaint;
    private LinearGradient linearGradient;

    void updateFromShape(Shape shape) {
        updateFromPaint(shape.getFill());
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

    public Paint getSwingPaint() {
        return swingPaint;
    }
}
