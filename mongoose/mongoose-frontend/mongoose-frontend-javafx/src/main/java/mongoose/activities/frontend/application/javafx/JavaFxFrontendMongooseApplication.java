package mongoose.activities.frontend.application.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.activities.frontend.application.FrontendMongooseApplication;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.JavaFxToolkit;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class JavaFxFrontendMongooseApplication {

    public static void main(String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        FrontendMongooseApplication.main(args);
        FrontendMongooseApplication.setLoadingSpinnerVisibleConsumer(JavaFxFrontendMongooseApplication::setLoadingSpinnerVisible);
    }

    private static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("mongoose/client/java/css/mongoose.css"));
    }

    private static ImageView spinner;

    private static void setLoadingSpinnerVisible(boolean visible) {
        Scene scene = Toolkit.get().getPrimaryStage().getScene();
        Node root = scene == null ? null : scene.getRoot();
        if (root != null) {
            javafx.scene.Node fxNode = root; //((FxNodePeer) root.getOrCreateAndBindNodePeer()).getFxNode();
            if (fxNode instanceof Pane) {
                Pane rootPane = (Pane) fxNode;
                if (!visible) {
                    rootPane.getChildren().remove(spinner);
                } else if (!rootPane.getChildren().contains(spinner)) {
                    if (spinner == null)
                        spinner = ImageStore.createImageView("mongoose/client/java/images/spinner.gif");
                    spinner.setManaged(false);
                    spinner.setX(rootPane.getWidth()  / 2 - spinner.prefWidth(-1)  / 2);
                    spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                    rootPane.getChildren().add(spinner);
                }
            }
        }
    }
}
