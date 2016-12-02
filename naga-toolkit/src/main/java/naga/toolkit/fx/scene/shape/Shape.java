package naga.toolkit.fx.scene.shape;

import javafx.collections.ObservableList;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface Shape extends Node,
        HasFillProperty,
        HasSmoothProperty,
        HasStrokeProperty,
        HasStrokeWidthProperty,
        HasStrokeLineCapProperty,
        HasStrokeLineJoinProperty,
        HasStrokeMiterLimitProperty,
        HasStrokeDashOffsetProperty {

    ObservableList<Double> getStrokeDashArray();
}
