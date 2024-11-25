package javafx.scene.shape;

import dev.webfx.kit.mapper.peers.javafxgraphics.markers.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

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

    private final ObjectProperty<Paint> fillProperty = new SimpleObjectProperty<>(/* null for CSS */);
    @Override
    public ObjectProperty<Paint> fillProperty() {
        return fillProperty;
    }

    private final BooleanProperty smoothProperty = new SimpleBooleanProperty(true);
    @Override
    public BooleanProperty smoothProperty() {
        return smoothProperty;
    }

    private final ObjectProperty<Paint> strokeProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<Paint> strokeProperty() {
        return strokeProperty;
    }

    private final ObjectProperty<StrokeType> strokeTypeProperty = new SimpleObjectProperty<>(StrokeType.CENTERED);
    @Override
    public ObjectProperty<StrokeType> strokeTypeProperty() {
        return strokeTypeProperty;
    }

    private final DoubleProperty strokeWidthProperty = new SimpleDoubleProperty(1d);
    @Override
    public DoubleProperty strokeWidthProperty() {
        return strokeWidthProperty;
    }

    private final ObjectProperty<StrokeLineCap> strokeLineCapProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<StrokeLineCap> strokeLineCapProperty() {
        return strokeLineCapProperty;
    }

    private final ObjectProperty<StrokeLineJoin> strokeLineJoinProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<StrokeLineJoin> strokeLineJoinProperty() {
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

    public static Shape subtract(final Shape shape1, final Shape shape2) {
        return new SubtractShape(shape1, shape2);
    }
}
