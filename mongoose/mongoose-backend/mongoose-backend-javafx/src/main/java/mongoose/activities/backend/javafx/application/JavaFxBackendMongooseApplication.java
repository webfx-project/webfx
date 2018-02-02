package mongoose.activities.backend.javafx.application;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.activities.backend.application.BackendMongooseApplication;
import mongoose.activities.backend.javafx.event.clone.FxCloneEventRouting;
import mongoose.activities.shared.application.SharedMongooseApplication;
import naga.framework.ui.router.UiRouter;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.JavaFxToolkit;
import naga.fx.util.ImageStore;


/**
 * @author Bruno Salmon
 */
public class JavaFxBackendMongooseApplication extends BackendMongooseApplication {

    public static void main(String[] args) {
        launchJavaFxBackendMongooseApplication(args);
    }

    public static void launchJavaFxBackendMongooseApplication(String[] args) {
        launchJavaFxMongooseApplication(new JavaFxBackendMongooseApplication(), args);
    }

    public static void launchJavaFxMongooseApplication(SharedMongooseApplication mongooseApplication, String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        launchApplication(mongooseApplication, args);
        setLoadingSpinnerVisibleConsumer(JavaFxBackendMongooseApplication::setLoadingSpinnerVisible);
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter.
                route(FxCloneEventRouting.uiRoute())
        );
    }

    private static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("mongoose/client/java/css/mongoose.css"));
    }

    private static ImageView spinner;

    private static void setLoadingSpinnerVisible(boolean visible) {
        Scene scene = Toolkit.get().getPrimaryStage().getScene();
        Node root = scene == null ? null : scene.getRoot();
        if (root instanceof Pane) {
            Pane rootPane = (Pane) root;
            if (!visible) {
                rootPane.getChildren().remove(spinner);
            } else if (!rootPane.getChildren().contains(spinner)) {
                if (spinner == null)
                    spinner = ImageStore.createImageView("mongoose/client/java/images/spinner.gif");
                spinner.setManaged(false);
                spinner.setX(rootPane.getWidth() / 2 - spinner.prefWidth(-1) / 2);
                spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                rootPane.getChildren().add(spinner);
            }
        }
    }
}
