package naga.providers.toolkit.javafx.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.layouts.Window;

/**
 * @author Bruno Salmon
 */
public class FxWindow implements Window<Parent> {

    protected Stage stage;

    public FxWindow(Stage stage) {
        setStage(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage != null) {
            if (nodeProperty.getValue() != null)
                setWindowContent(nodeProperty.getValue().unwrapToNativeNode());
            nodeProperty.addListener((observable, oldValue, newValue) -> { if (newValue != null) setWindowContent(newValue.unwrapToNativeNode()); });
            titleProperty().addListener((observable, oldValue, newValue) -> stage.setTitle(newValue));
        }
    }

    private void setWindowContent(Parent rootComponent) {
        Scene scene = stage.getScene();
        if (scene != null)
            scene.setRoot(rootComponent);
        else { // Creating the scene if not yet done
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setScene(scene = createScene(rootComponent, screenBounds.getWidth() * 0.8, screenBounds.getHeight() * 0.9));
            // Calling the scene hook is specified
            Consumer<Scene> sceneHook = JavaFxToolkit.getSceneHook();
            if (sceneHook != null)
                sceneHook.accept(scene);
        }
        stage.show();
    }

    protected Scene createScene(Parent rootComponent, double width, double height) {
        return new Scene(rootComponent, width, height);
    }

    private final Property<GuiNode<Parent>> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<GuiNode<Parent>> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }

}
