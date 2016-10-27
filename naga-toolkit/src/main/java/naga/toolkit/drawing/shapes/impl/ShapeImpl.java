package naga.toolkit.drawing.shapes.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.StrokeLineCap;
import naga.toolkit.drawing.shapes.StrokeLineJoin;

/**
 * @author Bruno Salmon
 */
public class ShapeImpl implements Shape {

    private final Property<Paint> fillProperty = new SimpleObjectProperty<>(Color.TRANSPARENT);
    @Override
    public Property<Paint> fillProperty() {
        return fillProperty;
    }

    private final Property<Boolean> smoothProperty = new SimpleObjectProperty<>(true);
    @Override
    public Property<Boolean> smoothProperty() {
        return smoothProperty;
    }

    private final Property<Paint> stokeProperty = new SimpleObjectProperty<>(Color.TRANSPARENT);
    @Override
    public Property<Paint> strokeProperty() {
        return stokeProperty;
    }

    private final Property<Double> strokeWidthProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> strokeWidthProperty() {
        return strokeWidthProperty;
    }

    private final Property<StrokeLineCap> strokeLineCapProperty = new SimpleObjectProperty<>();
    @Override
    public Property<StrokeLineCap> strokeLineCapProperty() {
        return strokeLineCapProperty;
    }

    private final Property<StrokeLineJoin> strokeLineJoinProperty = new SimpleObjectProperty<>();
    @Override
    public Property<StrokeLineJoin> strokeLineJoinProperty() {
        return strokeLineJoinProperty;
    }

    private final Property<Double> strokeMiterLimitProperty = new SimpleObjectProperty<>(1d);
    @Override
    public Property<Double> strokeMiterLimitProperty() {
        return strokeMiterLimitProperty;
    }

    private final Property<Double> strokeDashOffsetProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> strokeDashOffsetProperty() {
        return strokeDashOffsetProperty;
    }

    private final ObservableList<Double> getStrokeDashArray = FXCollections.observableArrayList();
    @Override
    public ObservableList<Double> getStrokeDashArray() {
        return getStrokeDashArray;
    }
}
