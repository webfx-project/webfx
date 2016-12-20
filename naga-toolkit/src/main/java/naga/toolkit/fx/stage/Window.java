package naga.toolkit.fx.stage;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import naga.toolkit.fx.scene.Scene;
import naga.toolkit.properties.markers.HasHeightProperty;
import naga.toolkit.properties.markers.HasSceneProperty;
import naga.toolkit.properties.markers.HasTitleProperty;
import naga.toolkit.properties.markers.HasWidthProperty;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public abstract class Window implements
        HasSceneProperty,
        HasTitleProperty,
        HasWidthProperty,
        HasHeightProperty {

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

    private void onSceneUpdate() {
        Scene scene = getScene();
        if (scene != null) {
            // Binding the scene dimensions to the window dimensions (however the window dimension is initialized with the scene dimension if possible)
            bindSceneDimension(getScene().widthProperty(), widthProperty);
            bindSceneDimension(getScene().heightProperty(), heightProperty);
            // Finally binding the root node
            Properties.runNowAndOnPropertiesChange(p -> onSceneRootUpdate(), scene.rootProperty());
        }
    }

    private void bindSceneDimension(Property<Double> sceneDimension, Property<Double> windowDimension) {
        if (sceneDimension.getValue() == 0) // means that the scene dimension has not been set yet
            sceneDimension.addListener(new ChangeListener<Double>() {
                @Override
                public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
                    observable.removeListener(this);
                    bindSceneDimension(sceneDimension, windowDimension);
                }
            });
        else {
            // Initially the window dimension is set to the scene dimension (if possible - will work on desktop but not on browser)
            Properties.setIfNotBound(windowDimension, sceneDimension.getValue());
            // Then the scene dimension is bound to the window dimension (if the user resize the window, this will resize the scene)
            sceneDimension.bind(windowDimension);
        }
    }

    protected abstract void onSceneRootUpdate();

}
