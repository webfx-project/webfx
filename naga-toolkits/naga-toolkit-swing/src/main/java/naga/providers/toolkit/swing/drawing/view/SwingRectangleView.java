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

    private Paint swingFill;
    private boolean fillIsProportionalGradient;
    private Stroke swingStroke;
    private Paint swingStrokePaint;
    private boolean strokePaintIsProportionalGradient;

    @Override
    public void bind(Rectangle shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        shape.fillProperty().addListener((observable, oldValue, newValue) -> updateSwingFill());
        updateSwingFill();
        shape.strokeProperty().addListener((observable, oldValue, newValue) -> updateSwingStroke());
        shape.strokeWidthProperty().addListener((observable, oldValue, newValue) -> updateSwingStroke());
        updateSwingStroke();
    }

    private void updateSwingFill() {
        naga.toolkit.drawing.paint.Paint fill = shape.getFill();
        fillIsProportionalGradient = fill instanceof LinearGradient && ((LinearGradient) fill).isProportional();
        swingFill = fillIsProportionalGradient ? null : SwingPaints.toSwingPaint(fill);
    }

    private void updateSwingStroke() {
        naga.toolkit.drawing.paint.Paint stroke = shape.getStroke();
        swingStroke = new BasicStroke(shape.getStrokeWidth().intValue());
        strokePaintIsProportionalGradient = stroke instanceof LinearGradient && ((LinearGradient) stroke).isProportional();
        swingStrokePaint = strokePaintIsProportionalGradient ? null : SwingPaints.toSwingPaint(stroke);
    }

    @Override
    public void paintShape(Graphics g) {
        java.awt.Rectangle swingRectangle = new java.awt.Rectangle(shape.getX().intValue(), shape.getY().intValue(), shape.getWidth().intValue(), shape.getHeight().intValue());
        Graphics2D g2 = (Graphics2D) g;
        if (fillIsProportionalGradient)
            swingFill = SwingPaints.toSwingLinearGradient((LinearGradient) shape.getFill(), shape.getWidth().floatValue(), shape.getHeight().floatValue());
        g2.setPaint(swingFill);
        g2.fill(swingRectangle);
        if (swingStroke != null) {
            g2.setStroke(swingStroke);
            if (strokePaintIsProportionalGradient)
                swingStrokePaint = SwingPaints.toSwingLinearGradient((LinearGradient) shape.getStroke(), shape.getWidth().floatValue(), shape.getHeight().floatValue());
            g2.setPaint(swingStrokePaint);
            g2.draw(swingRectangle);
        }
    }
}
