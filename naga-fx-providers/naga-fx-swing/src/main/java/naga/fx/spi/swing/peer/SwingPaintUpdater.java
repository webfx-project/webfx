package naga.fx.spi.swing.peer;

import naga.fx.spi.swing.util.SwingPaints;
import naga.fx.scene.paint.LinearGradient;
import naga.fx.scene.shape.Shape;

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

    void updateFromPaint(naga.fx.scene.paint.Paint paint) {
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
