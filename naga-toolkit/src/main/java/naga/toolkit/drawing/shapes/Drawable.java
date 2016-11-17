package naga.toolkit.drawing.shapes;

import javafx.collections.ObservableList;
import naga.toolkit.properties.markers.HasLayoutXProperty;
import naga.toolkit.properties.markers.HasLayoutYProperty;
import naga.toolkit.properties.markers.HasOnMouseClickedProperty;
import naga.toolkit.transform.Transform;

import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public interface Drawable extends
        HasOnMouseClickedProperty,
        HasLayoutXProperty,
        HasLayoutYProperty {

    ObservableList<Transform> getTransforms();

    Collection<Transform> localToParentTransforms();

    default void relocate(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
