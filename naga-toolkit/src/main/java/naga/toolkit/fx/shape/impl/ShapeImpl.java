package naga.toolkit.fx.shape.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.toolkit.fx.paint.Color;
import naga.toolkit.fx.paint.Paint;
import naga.toolkit.fx.scene.impl.NodeImpl;
import naga.toolkit.fx.shape.Shape;
import naga.toolkit.fx.shape.StrokeLineCap;
import naga.toolkit.fx.shape.StrokeLineJoin;

/**
 * @author Bruno Salmon
 */
public abstract class ShapeImpl extends NodeImpl implements Shape {

    private final Property<Paint> fillProperty = new SimpleObjectProperty<>(Color.BLACK);
    @Override
    public Property<Paint> fillProperty() {
        return fillProperty;
    }

    private final Property<Boolean> smoothProperty = new SimpleObjectProperty<>(true);
    @Override
    public Property<Boolean> smoothProperty() {
        return smoothProperty;
    }

    private final Property<Paint> stokeProperty = new SimpleObjectProperty<>();
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
