package naga.toolkit.drawing.shapes.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.drawing.shapes.Circle;

/**
 * @author Bruno Salmon
 */
public class CircleImpl extends ShapeImpl implements Circle {

    public CircleImpl() {
    }

    public CircleImpl(double radius) {
        this(0, 0, radius);
    }

    public CircleImpl(double centerX, double centerY, double radius) {
        setCenterX(centerX);
        setCenterY(centerY);
        setRadius(radius);
    }

    private final Property<Double> centerXProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> centerXProperty() {
        return centerXProperty;
    }

    private final Property<Double> centerYProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> centerYProperty() {
        return centerYProperty;
    }

    private final Property<Double> radiusProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Double> radiusProperty() {
        return radiusProperty;
    }

}
