package mongoose.activities.backend.application.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.activities.backend.application.MongooseBackendApplication;
import mongoose.activities.backend.event.clone.FxCloneEventPresentationActivity;
import naga.framework.activity.view.presentation.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRouter;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.JavaFxToolkit;
import naga.fx.spi.javafx.util.FxImageStore;


/**
 * @author Bruno Salmon
 */
public class JavaFxMongooseBackendApplication extends MongooseBackendApplication {

    public static void main(String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        launchApplication(new JavaFxMongooseBackendApplication(), args);
        setLoadingSpinnerVisibleConsumer(JavaFxMongooseBackendApplication::setLoadingSpinnerVisible);
    }

    @Override
    protected UiRouter setupContainedRouter(UiRouter containedRouter) {
        return super.setupContainedRouter(containedRouter.
                route("/event/:eventId/clone", FxCloneEventPresentationActivity::new, DomainPresentationActivityContextFinal::new)
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
                    spinner = FxImageStore.createIconImageView("mongoose/client/java/images/spinner.gif");
                spinner.setManaged(false);
                spinner.setX(rootPane.getWidth() / 2 - spinner.prefWidth(-1) / 2);
                spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                rootPane.getChildren().add(spinner);
            }
        }
    }
}
