package mongoose.client.application;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.activity.ActivityManager;
import webfx.framework.activity.impl.combinations.viewdomainapplication.ViewDomainApplicationContext;
import webfx.framework.ui.layouts.SceneUtil;
import webfx.fxkits.core.launcher.FxKitLauncher;
import webfx.fxkits.extra.util.ImageStore;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public abstract class MongooseClientApplicationModuleInitializer implements ApplicationModuleInitializer {

    private final MongooseClientApplication mongooseApplication;

    public MongooseClientApplicationModuleInitializer(MongooseClientApplication mongooseApplication) {
        this.mongooseApplication = mongooseApplication;
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        SceneUtil.onPrimarySceneReady(scene -> scene.getStylesheets().addAll("mongoose/client/css/mongoose.css"));
        ActivityManager.runActivity(mongooseApplication,
                ViewDomainApplicationContext.createViewDomainApplicationContext(
                        DomainModelSnapshotLoader.getDataSourceModel(),
                        null //args
                )
        );
        MongooseClientApplication.setLoadingSpinnerVisibleConsumer(this::setLoadingSpinnerVisible);
    }

    private ImageView spinner;

    private void setLoadingSpinnerVisible(boolean visible) {
        Scene scene = FxKitLauncher.getPrimaryStage().getScene();
        Node root = scene == null ? null : scene.getRoot();
        if (root instanceof Pane) {
            Pane rootPane = (Pane) root;
            if (!visible) {
                rootPane.getChildren().remove(spinner);
            } else if (!rootPane.getChildren().contains(spinner)) {
                if (spinner == null)
                    spinner = ImageStore.createImageView("mongoose/client/images/spinner.gif");
                spinner.setManaged(false);
                spinner.setX(rootPane.getWidth() / 2 - spinner.prefWidth(-1) / 2);
                spinner.setY(rootPane.getHeight() / 2 - spinner.prefHeight(-1) / 2);
                rootPane.getChildren().add(spinner);
            }
        }
    }
}
