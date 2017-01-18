package naga.fx.spi.swing.peer;

import naga.commons.util.Numbers;
import naga.commons.util.collection.Collections;
import naga.fx.spi.swing.util.SwingStrokes;
import emul.javafx.scene.layout.Border;
import emul.javafx.scene.layout.BorderStroke;
import emul.javafx.scene.layout.BorderStrokeStyle;
import emul.javafx.scene.shape.Shape;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
class SwingStrokeUpdater extends SwingPaintUpdater {

    private Stroke swingStroke;
    private Shape shape;
    private Border border;

    @Override
    protected void updateFromShape(Shape shape) {
        swingStroke = null;
        this.shape = shape;
    }

    void updateFromBorder(Border border) {
        swingStroke = null;
        this.border = border;
    }

    Stroke getSwingStroke() {
        if (swingStroke == null) {
            if (shape != null && shape.getStroke() != null) {
                updateFromPaint(shape.getStroke());
                swingStroke = new BasicStroke(shape.getStrokeWidth().intValue(),
                        SwingStrokes.toSwingStrokeLineCap(shape.getStrokeLineCap()),
                        SwingStrokes.toSwingStrokeLineJoin(shape.getStrokeLineJoin()),
                        Numbers.floatValue(shape.getStrokeMiterLimit()),
                        SwingStrokes.toSwingDashArray(shape.getStrokeDashArray()),
                        Numbers.floatValue(shape.getStrokeDashOffset()));
                shape = null;
            } else if (border != null) {
                BorderStroke firstStroke = Collections.get(border.getStrokes(), 0);
                if (firstStroke != null) {
                    updateFromPaint(firstStroke.getLeftStroke());
                    BorderStrokeStyle style = firstStroke.getLeftStyle();
                    swingStroke = new BasicStroke((int) firstStroke.getWidths().getLeft(),
                            SwingStrokes.toSwingStrokeLineCap(style.getLineCap()),
                            SwingStrokes.toSwingStrokeLineJoin(style.getLineJoin()),
                            Numbers.floatValue(style.getMiterLimit()),
                            SwingStrokes.toSwingDashArray(style.getDashArray()),
                            Numbers.floatValue(style.getDashOffset()));
                }
                border = null;
            }
        }
        return swingStroke;
    }
}
