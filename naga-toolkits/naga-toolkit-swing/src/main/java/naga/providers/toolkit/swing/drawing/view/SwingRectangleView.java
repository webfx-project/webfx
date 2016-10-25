package naga.providers.toolkit.swing.drawing.view;

import naga.commons.util.collection.Collections;
import naga.providers.toolkit.swing.util.SwingPaints;
import naga.providers.toolkit.swing.util.SwingStrokes;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

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
        swingStroke = new BasicStroke(shape.getStrokeWidth().intValue(),
                SwingStrokes.toSwingStrokeLineCap(shape.getStrokeLineCap()),
                SwingStrokes.toSwingStrokeLineJoin(shape.getStrokeLineJoin()),
                shape.getStrokeMiterLimit().floatValue(),
                Collections.doubleCollectionToFloatArray(shape.getStrokeDashArray()),
                shape.getStrokeDashOffset().floatValue());
        strokePaintIsProportionalGradient = stroke instanceof LinearGradient && ((LinearGradient) stroke).isProportional();
        swingStrokePaint = strokePaintIsProportionalGradient ? null : SwingPaints.toSwingPaint(stroke);
    }

    @Override
    public void paintShape(Graphics g) {
        Double arcWidth = shape.getArcWidth();
        Double arcHeight = shape.getArcHeight();
        java.awt.Shape swingRectangle;
        if (arcWidth != null && arcHeight != null && arcWidth != 0 && arcHeight != 0)
            swingRectangle = new RoundRectangle2D.Double(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), arcWidth, arcHeight);
        else
            swingRectangle = new Rectangle2D.Double(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
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
