package mongoose.client.application;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.client.activity.ActivityManager;
import webfx.framework.client.activity.impl.combinations.viewdomainapplication.ViewDomainApplicationContext;
import webfx.framework.client.ui.layouts.SceneUtil;
import webfx.fxkit.launcher.FxKitLauncher;
import webfx.fxkit.util.properties.Properties;
import webfx.fxkit.extra.util.ImageStore;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;

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
        // Activating focus owner auto scroll
        SceneUtil.installPrimarySceneFocusOwnerAutoScroll();
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
                if (spinner == null) {
                    spinner = ImageStore.createImageView("mongoose/client/images/spinner.gif");
                    spinner.setManaged(false);
                }
                spinner.xProperty().bind(Properties.combine(rootPane.widthProperty(), spinner.getImage().widthProperty(), (w1, w2) -> (w1.doubleValue() - w2.doubleValue())/2 ));
                spinner.yProperty().bind(Properties.combine(rootPane.heightProperty(), spinner.getImage().heightProperty(), (h1, h2) -> (h1.doubleValue() - h2.doubleValue())/2 ));
                rootPane.getChildren().add(spinner);
            }
        }
    }
}
