package emul.javafx.scene.shape;

import emul.javafx.beans.property.Property;
import emul.javafx.beans.property.SimpleObjectProperty;
import emul.javafx.collections.FXCollections;
import emul.javafx.collections.ObservableList;
import emul.javafx.scene.Node;
import emul.javafx.scene.paint.Color;
import emul.javafx.scene.paint.Paint;
import webfx.fxkits.core.mapper.spi.impl.peer.markers.*;

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
