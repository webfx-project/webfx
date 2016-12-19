package naga.toolkit.fx.scene.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.fx.stage.Window;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class WindowImpl implements Window {

    private final Property<Scene> sceneProperty = new SimpleObjectProperty<Scene>() {
        @Override
        protected void invalidated() {
            onSceneUpdate();
        }
    };
    @Override
    public Property<Scene> sceneProperty() {
        return sceneProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<String>() {
        @Override
        protected void invalidated() {
            onTitleUpdate();
        }
    };
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }

    private final Property<Double> widthProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> widthProperty() {
        return widthProperty;
    }

    private final Property<Double> heightProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> heightProperty() {
        return heightProperty;
    }

    protected abstract void onTitleUpdate();

    protected void onSceneUpdate() {
        Properties.runNowAndOnPropertiesChange(p -> onSceneRootUpdate(), getScene().rootProperty());
    }

    protected abstract void onSceneRootUpdate();

}
