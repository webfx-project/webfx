package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingPaints;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleView extends RectangleViewImplBase implements SwingShapeView {

    private Paint paint;
    private boolean proportionalGradient;

    @Override
    public void bind(Rectangle shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        updatePaint();
        shape.fillProperty().addListener((observable, oldValue, newValue) -> updatePaint());
    }

    private void updatePaint() {
        naga.toolkit.drawing.paint.Paint fill = shape.getFill();
        proportionalGradient = fill instanceof LinearGradient && ((LinearGradient) fill).isProportional();
        paint = SwingPaints.toSwingPaint(fill);
    }

    @Override
    public void paintShape(Graphics g) {
        if (proportionalGradient)
            paint = SwingPaints.toSwingLinearGradient((LinearGradient) shape.getFill(), shape.getWidth().floatValue(), shape.getHeight().floatValue());
        ((Graphics2D) g).setPaint(paint);
        g.fillRect(shape.getX().intValue(), shape.getY().intValue(), shape.getWidth().intValue(), shape.getHeight().intValue());
    }
}
