package mongoose.backend.javafx.activities;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongooses.core.activities.backend.MongooseBackendApplication;
import mongoose.backend.javafx.activities.event.clone.FxCloneEventRouting;
import mongooses.core.activities.sharedends.MongooseSharedEndsApplication;
import webfx.framework.ui.uirouter.UiRouter;
import webfx.fxkits.core.spi.FxKit;
import webfx.fxkit.javafx.JavaFxFxKit;
import webfx.fxkits.core.util.ImageStore;


/**
 * @author Bruno Salmon
 */
public final class MongooseBackendJavaFxApplication extends MongooseBackendApplication {

    public static void main(String[] args) {
        launchJavaFxBackendMongooseApplication(args);
    }

    public static void launchJavaFxBackendMongooseApplication(String[] args) {
        launchJavaFxMongooseApplication(new MongooseBackendJavaFxApplication(), args);
    }

    public static void launchJavaFxMongooseApplication(MongooseSharedEndsApplication mongooseApplication, String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        launchApplication(mongooseApplication, args);
        setLoadingSpinnerVisibleConsumer(MongooseBackendJavaFxApplication::setLoadingSpinnerVisible);
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter.
                route(FxCloneEventRouting.uiRoute())
        );
    }

    private static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxFxKit.setSceneHook(scene -> scene.getStylesheets().addAll("mongooses/java/client/css/mongoose.css"));
    }

    private static ImageView spinner;

    private static void setLoadingSpinnerVisible(boolean visible) {
        Scene scene = FxKit.get().getPrimaryStage().getScene();
        Node root = scene == null ? null : scene.getRoot();
        if (root instanceof Pane) {
            Pane rootPane = (Pane) root;
            if (!visible) {
                rootPane.getChildren().remove(spinner);
            } else if (!rootPane.getChildren().contains(spinner)) {
                if (spinner == null)
                    spinner = ImageStore.createImageView("mongooses/java/client/images/spinner.gif");
                spinner.setManaged(false);
                spinner.setX(rootPane.getWidth() / 2 - spinner.prefWidth(-1) / 2);
                spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                rootPane.getChildren().add(spinner);
            }
        }
    }
}
