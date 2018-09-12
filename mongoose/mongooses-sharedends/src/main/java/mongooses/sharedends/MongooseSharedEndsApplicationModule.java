package mongooses.sharedends;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongooses.core.activities.sharedends.MongooseSharedEndsApplication;
import mongooses.core.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.activity.ActivityManager;
import webfx.framework.activity.base.combinations.viewdomainapplication.ViewDomainApplicationContext;
import webfx.fxkits.core.spi.FxKit;
import webfx.fxkits.extra.util.ImageStore;
import webfx.platforms.core.services.bus.call.BusBasedClientApplicationModuleBase;

/**
 * @author Bruno Salmon
 */
public class MongooseSharedEndsApplicationModule extends BusBasedClientApplicationModuleBase {

    private final MongooseSharedEndsApplication mongooseApplication;

    public MongooseSharedEndsApplicationModule(MongooseSharedEndsApplication mongooseApplication) {
        this.mongooseApplication = mongooseApplication;
    }

    @Override
    public void start() {
        super.start();
        ActivityManager.startActivityAsApplicationService(mongooseApplication,
                ViewDomainApplicationContext.createViewDomainApplicationContext(
                        DomainModelSnapshotLoader.getDataSourceModel(),
                        null //args
                )
        );
        MongooseSharedEndsApplication.setLoadingSpinnerVisibleConsumer(this::setLoadingSpinnerVisible);
    }

    private static void installSceneHook() {
        // Setting JavaFx scene hook to apply the mongoose css file
        // FxKit.setSceneHook(scene -> scene.getStylesheets().addAll("mongooses/java/client/css/mongoose.css"));
    }

    boolean cssApplied;
    private ImageView spinner;

    private void setLoadingSpinnerVisible(boolean visible) {
        Scene scene = FxKit.get().getPrimaryStage().getScene();
        if (scene != null && !cssApplied) {
            scene.getStylesheets().addAll("mongooses/java/client/css/mongoose.css");
            cssApplied = true;
        }
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
