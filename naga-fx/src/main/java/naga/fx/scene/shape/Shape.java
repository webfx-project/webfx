package naga.fx.scene.shape;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import naga.fx.scene.Node;
import naga.fx.scene.paint.Color;
import naga.fx.scene.paint.Paint;
import naga.fx.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public abstract class Shape extends Node implements
        HasFillProperty,
        HasSmoothProperty,
        HasStrokeProperty,
        HasStrokeTypeProperty,
        HasStrokeWidthProperty,
        HasStrokeLineCapProperty,
        HasStrokeLineJoinProperty,
        HasStrokeMiterLimitProperty,
        HasStrokeDashOffsetProperty {

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

    private final Property<StrokeType> strokeTypeProperty = new SimpleObjectProperty<>(StrokeType.CENTERED);
    @Override
    public Property<StrokeType> strokeTypeProperty() {
        return strokeTypeProperty;
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
    public ObservableList<Double> getStrokeDashArray() {
        return getStrokeDashArray;
    }
}
