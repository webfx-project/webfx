package mongoose.client.backend.javafx;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.activities.backend.application.MongooseBackendApplication;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.fx.viewer.FxNodeViewer;
import naga.providers.toolkit.javafx.util.FxImageStore;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.spi.Toolkit;


/**
 * @author Bruno Salmon
 */
public class MongooseBackendJavaFxApplication {

    public static void main(String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        MongooseBackendApplication.main(args);
        MongooseBackendApplication.setLoadingSpinnerVisibleConsumer(MongooseBackendJavaFxApplication::setLoadingSpinnerVisible);
    }

    private static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("mongoose/client/java/css/mongoose.css"));
    }

    private static ImageView spinner;

    private static void setLoadingSpinnerVisible(boolean visible) {
        Node node = Toolkit.get().getApplicationWindow().getNode();
        if (node != null) {
            javafx.scene.Node fxNode = ((FxNodeViewer) node.getOrCreateAndBindNodeViewer()).getFxNode();
            if (fxNode instanceof Pane) {
                Pane rootPane = (Pane) fxNode;
                if (!visible) {
                    rootPane.getChildren().remove(spinner);
                } else if (!rootPane.getChildren().contains(spinner)) {
                    if (spinner == null)
                        spinner = FxImageStore.createIconImageView("mongoose/client/java/images/spinner.gif");
                    spinner.setManaged(false);
                    spinner.setX(rootPane.getWidth()  / 2 - spinner.prefWidth(-1)  / 2);
                    spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                    rootPane.getChildren().add(spinner);
                }
            }
        }
    }
}
