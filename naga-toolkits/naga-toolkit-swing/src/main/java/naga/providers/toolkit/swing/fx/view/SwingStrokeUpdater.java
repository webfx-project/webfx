package naga.providers.toolkit.swing.fx.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.swing.util.SwingStrokes;
import naga.toolkit.fx.scene.shape.Shape;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingStrokeUpdater extends SwingPaintUpdater {

    private Stroke swingStroke;
    private Shape shape;

    @Override
    protected void updateFromShape(Shape shape) {
        swingStroke = null;
        this.shape = shape;
    }

    Stroke getSwingStroke() {
        if (swingStroke == null && shape != null && shape.getStroke() != null) {
            updateFromPaint(shape.getStroke());
            swingStroke = new BasicStroke(shape.getStrokeWidth().intValue(),
                    SwingStrokes.toSwingStrokeLineCap(shape.getStrokeLineCap()),
                    SwingStrokes.toSwingStrokeLineJoin(shape.getStrokeLineJoin()),
                    Numbers.floatValue(shape.getStrokeMiterLimit()),
                    SwingStrokes.toSwingDashArray(shape.getStrokeDashArray()),
                    Numbers.floatValue(shape.getStrokeDashOffset()));
            shape = null;
        }
        return swingStroke;
    }
}
