package naga.toolkit.fx.scene.shape;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geom.BaseBounds;
import naga.toolkit.fx.geom.transform.BaseTransform;
import naga.toolkit.fx.properties.markers.HasEndXProperty;
import naga.toolkit.fx.properties.markers.HasEndYProperty;
import naga.toolkit.fx.properties.markers.HasStartXProperty;
import naga.toolkit.fx.properties.markers.HasStartYProperty;

/**
 * @author Bruno Salmon
 */
public class Line extends Shape implements
        HasStartXProperty,
        HasStartYProperty,
        HasEndXProperty,
        HasEndYProperty {

    public Line() {
    }

    private final Property<Double> startXProperty = new SimpleObjectProperty<>(0d);

    @Override
    public Property<Double> startXProperty() {
        return startXProperty;
    }

    private final Property<Double> startYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> startYProperty() {
        return startYProperty;
    }

    private final Property<Double> endXProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> endXProperty() {
        return endXProperty;
    }

    private final Property<Double> endYProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> endYProperty() {
        return endYProperty;
    }

    @Override
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {

/*
        // Since line's only draw with strokes, if the mode is FILL or EMPTY
        // then we simply return empty bounds
        if (impl_mode == NGShape.Mode.FILL || impl_mode == NGShape.Mode.EMPTY ||
                getStrokeType() == StrokeType.INSIDE)
        {
            return bounds.makeEmpty();
        }
*/

        double x1 = getStartX();
        double x2 = getEndX();
        double y1 = getStartY();
        double y2 = getEndY();
        // Get the draw stroke, and figure out the bounds based on the stroke.
        double wpad = getStrokeWidth();
/*
        if (getStrokeType() == StrokeType.CENTERED) {
            wpad /= 2.0f;
        }
*/
        // fast path the case of AffineTransform being TRANSLATE or identity
        if (tx.isTranslateOrIdentity()) {
            final double xpad;
            final double ypad;
            wpad = Math.max(wpad, 0.5f);
/*
            if (tx.getType() == BaseTransform.TYPE_TRANSLATION) {
                final double ddx = tx.getMxt();
                final double ddy = tx.getMyt();
                x1 += ddx;
                y1 += ddy;
                x2 += ddx;
                y2 += ddy;
            }
*/
            if (y1 == y2 && x1 != x2) {
                ypad = wpad;
                xpad = (getStrokeLineCap() == StrokeLineCap.BUTT) ? 0.0f : wpad;
            } else if (x1 == x2 && y1 != y2) {
                xpad = wpad;
                ypad = (getStrokeLineCap() == StrokeLineCap.BUTT) ? 0.0f : wpad;
            } else {
                if (getStrokeLineCap() == StrokeLineCap.SQUARE) {
                    wpad *= Math.sqrt(2);
                }
                xpad = ypad = wpad;
            }
            if (x1 > x2) { final double t = x1; x1 = x2; x2 = t; }
            if (y1 > y2) { final double t = y1; y1 = y2; y2 = t; }

            x1 -= xpad;
            y1 -= ypad;
            x2 += xpad;
            y2 += ypad;
            bounds = bounds.deriveWithNewBounds((float)x1, (float)y1, 0.0f,
                    (float)x2, (float)y2, 0.0f);
            return bounds;
        }

        throw new UnsupportedOperationException("Line.impl_computeGeomBounds");

/*
        double dx = x2 - x1;
        double dy = y2 - y1;
        final double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0.0f) {
            dx = wpad;
            dy = 0.0f;
        } else {
            dx = wpad * dx / len;
            dy = wpad * dy / len;
        }
        final double ecx;
        final double ecy;
        if (getStrokeLineCap() != StrokeLineCap.BUTT) {
            ecx = dx;
            ecy = dy;
        } else {
            ecx = ecy = 0.0f;
        }
        final double corners[] = new double[] {
                x1-dy-ecx, y1+dx-ecy,
                x1+dy-ecx, y1-dx-ecy,
                x2+dy+ecx, y2-dx+ecy,
                x2-dy+ecx, y2+dx+ecy };
        tx.transform(corners, 0, corners, 0, 4);
        x1 = Math.min(Math.min(corners[0], corners[2]),
                Math.min(corners[4], corners[6]));
        y1 = Math.min(Math.min(corners[1], corners[3]),
                Math.min(corners[5], corners[7]));
        x2 = Math.max(Math.max(corners[0], corners[2]),
                Math.max(corners[4], corners[6]));
        y2 = Math.max(Math.max(corners[1], corners[3]),
                Math.max(corners[5], corners[7]));
        x1 -= 0.5f;
        y1 -= 0.5f;
        x2 += 0.5f;
        y2 += 0.5f;
        bounds = bounds.deriveWithNewBounds((float)x1, (float)y1, 0.0f,
                (float)x2, (float)y2, 0.0f);
        return bounds;
*/
    }
}
