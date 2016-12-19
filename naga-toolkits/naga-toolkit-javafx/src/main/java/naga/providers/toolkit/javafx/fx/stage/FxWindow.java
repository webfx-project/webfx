package naga.providers.toolkit.javafx.fx.stage;

import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.fx.viewer.FxNodeViewer;
import naga.toolkit.fx.scene.impl.WindowImpl;
import naga.toolkit.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class FxWindow extends WindowImpl {

    protected Stage stage;

    public FxWindow(Stage stage) {
        setStage(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (stage != null) {
            onTitleUpdate();
            onSceneRootUpdate();
        }
    }

    @Override
    protected void onTitleUpdate() {
        if (stage != null)
            stage.setTitle(getTitle());
    }

    @Override
    protected void onSceneRootUpdate() {
        if (stage != null)
            setWindowContent((Parent) ((FxNodeViewer) getScene().getRoot().getOrCreateAndBindNodeViewer()).getFxNode());
    }

    protected void setWindowContent(Parent rootComponent) {
        Toolkit.get().scheduler().runInUiThread(() -> {
            Scene scene = stage.getScene();
            if (scene != null)
                scene.setRoot(rootComponent);
            else { // Creating the scene if not yet done
                stage.setScene(scene = createScene(rootComponent, getScene().getWidth(), getScene().getHeight()));
                // Calling the scene hook is specified
                Consumer<Scene> sceneHook = JavaFxToolkit.getSceneHook();
                if (sceneHook != null)
                    sceneHook.accept(scene);
                stage.show();
            }
            fitWidthAndHeightWithContent();
        });
    }

    private void fitWidthAndHeightWithContent() {
        ObservableDoubleValue fitContentWidthProperty = getFitContentWidthProperty();
        ObservableDoubleValue fitContentHeightProperty = getFitContentHeightProperty();
        setWidth(fitContentWidthProperty.get());
        fitContentWidthProperty.addListener((observable, oldValue, newValue) -> setWidth(fitContentWidthProperty.get()));
        setHeight(fitContentHeightProperty.get());
        fitContentHeightProperty.addListener((observable, oldValue, newValue) -> setHeight(fitContentHeightProperty.get()));
    }

    protected ObservableDoubleValue getFitContentWidthProperty() {
        return stage.getScene().widthProperty();
    }

    protected ObservableDoubleValue getFitContentHeightProperty() {
        return stage.getScene().heightProperty();
    }

    protected Scene createScene(Parent rootComponent, double width, double height) {
        return new Scene(rootComponent, width, height);
    }

}
