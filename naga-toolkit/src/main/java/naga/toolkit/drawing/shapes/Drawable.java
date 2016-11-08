package naga.toolkit.drawing.shapes;

import javafx.collections.ObservableList;
import naga.toolkit.properties.markers.HasOnMouseClickedProperty;
import naga.toolkit.transform.Transform;

/**
 * @author Bruno Salmon
 */
public interface Drawable extends HasOnMouseClickedProperty {

    ObservableList<Transform> getTransforms();

}
