package naga.toolkit.drawing.shapes;

import javafx.collections.ObservableList;
import naga.toolkit.transform.Transform;

/**
 * @author Bruno Salmon
 */
public interface Drawable {

    ObservableList<Transform> getTransforms();

}
