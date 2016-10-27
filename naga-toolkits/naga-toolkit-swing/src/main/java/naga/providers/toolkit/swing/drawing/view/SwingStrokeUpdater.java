package naga.providers.toolkit.swing.drawing.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.swing.util.SwingStrokes;
import naga.toolkit.drawing.shapes.Shape;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingStrokeUpdater extends SwingPaintUpdater {

    Stroke swingStroke;

    @Override
    protected void updateFromShape(Shape shape) {
        updateFromPaint(shape.getStroke());
        swingStroke = new BasicStroke(shape.getStrokeWidth().intValue(),
                SwingStrokes.toSwingStrokeLineCap(shape.getStrokeLineCap()),
                SwingStrokes.toSwingStrokeLineJoin(shape.getStrokeLineJoin()),
                Numbers.floatValue(shape.getStrokeMiterLimit()),
                SwingStrokes.toSwingDashArray(shape.getStrokeDashArray()),
                Numbers.floatValue(shape.getStrokeDashOffset()));
    }
}
