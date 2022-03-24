package javafx.scene.shape;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;

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

    private final DoubleProperty strokeWidthProperty = new SimpleDoubleProperty(1d);
    @Override
    public DoubleProperty strokeWidthProperty() {
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

    private final DoubleProperty strokeMiterLimitProperty = new SimpleDoubleProperty(1d);
    @Override
    public DoubleProperty strokeMiterLimitProperty() {
        return strokeMiterLimitProperty;
    }

    private final DoubleProperty strokeDashOffsetProperty = new SimpleDoubleProperty(0d);
    @Override
    public DoubleProperty strokeDashOffsetProperty() {
        return strokeDashOffsetProperty;
    }

    private final ObservableList<Double> getStrokeDashArray = FXCollections.observableArrayList();
    public ObservableList<Double> getStrokeDashArray() {
        return getStrokeDashArray;
    }
}
