package naga.fx.properties.markers;

import javafx.beans.property.Property;
import naga.fx.scene.Scene;

/**
 * @author Bruno Salmon
 */
public interface HasSceneProperty {

    Property<Scene> sceneProperty();
    default void setScene(Scene scene) { sceneProperty().setValue(scene); }
    default Scene getScene() { return sceneProperty().getValue(); }
}
