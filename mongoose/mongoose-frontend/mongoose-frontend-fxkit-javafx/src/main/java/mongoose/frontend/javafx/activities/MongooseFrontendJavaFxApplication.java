package mongoose.frontend.javafx.activities;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import webfx.fxkit.javafx.JavaFxFxKitProvider;
import webfx.fxkits.core.FxKit;
import webfx.fxkits.extra.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendJavaFxApplication {

/*
    public static void main(String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        MongooseFrontendApplication.main(args);
        MongooseFrontendApplication.setLoadingSpinnerVisibleConsumer(MongooseFrontendJavaFxApplication::setLoadingSpinnerVisible);
    }
*/

    private static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxFxKitProvider.setSceneHook(scene -> scene.getStylesheets().addAll("mongooses/java/client/css/mongoose.css"));
    }

    private static ImageView spinner;

    private static void setLoadingSpinnerVisible(boolean visible) {
        Scene scene = FxKit.getPrimaryStage().getScene();
        Node root = scene == null ? null : scene.getRoot();
        if (root != null) {
            javafx.scene.Node fxNode = root; //((FxNodePeer) root.getOrCreateAndBindNodePeer()).getFxNode();
            if (fxNode instanceof Pane) {
                Pane rootPane = (Pane) fxNode;
                if (!visible) {
                    rootPane.getChildren().remove(spinner);
                } else if (!rootPane.getChildren().contains(spinner)) {
                    if (spinner == null)
                        spinner = ImageStore.createImageView("mongooses/java/client/images/spinner.gif");
                    spinner.setManaged(false);
                    spinner.setX(rootPane.getWidth()  / 2 - spinner.prefWidth(-1)  / 2);
                    spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                    rootPane.getChildren().add(spinner);
                }
            }
        }
    }
}
