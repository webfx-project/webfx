package naga.providers.toolkit.javafx.nodes.layouts;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.fx.FxDrawingNode;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.spi.nodes.layouts.Window;
import naga.toolkit.util.Properties;

/**
 * @author Bruno Salmon
 */
public class FxWindow implements Window {

    protected Stage stage;
    private final DrawingNode drawingNode = naga.toolkit.spi.Toolkit.get().createDrawingNode();

    public FxWindow(Stage stage) {
        setStage(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage != null) {
            Properties.runNowAndOnPropertiesChange(property -> setWindowContent(getNode()), nodeProperty);
            titleProperty().addListener((observable, oldValue, newValue) -> stage.setTitle(newValue));
            setWindowContent((Parent) ((FxDrawingNode) drawingNode).unwrapToNativeNode());
            ObservableDoubleValue fitContentWidthProperty = getFitContentWidthProperty();
            drawingNode.setWidth(fitContentWidthProperty.get());
            fitContentWidthProperty.addListener((observable, oldValue, newValue) -> drawingNode.setWidth(fitContentWidthProperty.get()));
            ObservableDoubleValue fitContentHeightProperty = getFitContentHeightProperty();
            drawingNode.setHeight(fitContentHeightProperty.get());
            fitContentHeightProperty.addListener((observable, oldValue, newValue) -> drawingNode.setHeight(fitContentHeightProperty.get()));
        }
    }

    private void setWindowContent(Node node) {
        drawingNode.setRootNode(node);
    }

    protected ObservableDoubleValue getFitContentWidthProperty() {
        return stage.getScene().widthProperty();
    }

    protected ObservableDoubleValue getFitContentHeightProperty() {
        return stage.getScene().heightProperty();
    }

    protected void setWindowContent(Parent rootComponent) {
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
            stage.show();
        }
    }

    protected Scene createScene(Parent rootComponent, double width, double height) {
        return new Scene(rootComponent, width, height);
    }

    private final Property<Node> nodeProperty = new SimpleObjectProperty<>();
    @Override
    public Property<Node> nodeProperty() {
        return nodeProperty;
    }

    private final Property<String> titleProperty = new SimpleObjectProperty<>();
    @Override
    public Property<String> titleProperty() {
        return titleProperty;
    }

}
