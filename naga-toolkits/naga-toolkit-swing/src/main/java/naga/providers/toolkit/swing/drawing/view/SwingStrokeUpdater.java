package naga.providers.toolkit.swing.drawing.view;

import javafx.beans.property.Property;
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
    protected boolean updateFromShape(Shape shape, Property changedProperty) {
        boolean hitChangedProperty = false;
        if (changedProperty == null || (hitChangedProperty = changedProperty == shape.strokeProperty()))
            updateFromPaint(shape.getStroke());
        if (swingStroke == null || !hitChangedProperty && (hitChangedProperty = changedProperty == shape.strokeWidthProperty() || changedProperty == shape.strokeLineCapProperty() || changedProperty == shape.strokeLineJoinProperty() || changedProperty == shape.strokeMiterLimitProperty() || changedProperty == shape.strokeDashOffsetProperty()))
            swingStroke = new BasicStroke(shape.getStrokeWidth().intValue(),
                    SwingStrokes.toSwingStrokeLineCap(shape.getStrokeLineCap()),
                    SwingStrokes.toSwingStrokeLineJoin(shape.getStrokeLineJoin()),
                    Numbers.floatValue(shape.getStrokeMiterLimit()),
                    SwingStrokes.toSwingDashArray(shape.getStrokeDashArray()),
                    Numbers.floatValue(shape.getStrokeDashOffset()));
        return hitChangedProperty;
    }
}
