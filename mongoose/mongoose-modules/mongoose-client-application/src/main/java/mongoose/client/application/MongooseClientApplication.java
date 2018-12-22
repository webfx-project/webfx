package mongoose.client.application;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mongoose.shared.domainmodel.loader.DomainModelSnapshotLoader;
import webfx.framework.client.activity.ActivityManager;
import webfx.framework.client.activity.impl.combinations.viewdomainapplication.ViewDomainApplicationContext;
import webfx.framework.client.ui.layouts.SceneUtil;
import webfx.fxkit.extra.util.ImageStore;
import webfx.fxkit.launcher.FxKitLauncher;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.services.buscall.PendingBusCall;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class MongooseClientApplication extends Application {

    private final MongooseClientActivity mongooseApplication;

    public MongooseClientApplication(MongooseClientActivity mongooseApplication) {
        this.mongooseApplication = mongooseApplication;
    }

    @Override
    public void init() {
        ActivityManager.runActivity(mongooseApplication,
                ViewDomainApplicationContext.createViewDomainApplicationContext(DomainModelSnapshotLoader.getDataSourceModel())
        );
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Rectangle2D screenVisualBounds = Screen.getPrimary().getVisualBounds();
        double width = screenVisualBounds.getWidth() * 0.8;
        double height = screenVisualBounds.getHeight() * 0.9;
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().addAll("mongoose/client/css/mongoose.css");
        //root.centerProperty().bind(mongooseApplication.nodeProperty()); //
        scene.rootProperty().bind(Properties.compute(mongooseApplication.nodeProperty(), n -> (Parent) n));
        // Activating focus owner auto scroll
        SceneUtil.installSceneFocusOwnerAutoScroll(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        setLoadingSpinnerVisibleConsumer(this::setLoadingSpinnerVisible);
    }

    private static void setLoadingSpinnerVisibleConsumer(Consumer<Boolean> consumer) {
        Properties.consumeInUiThread(Properties.compute(PendingBusCall.pendingCallsCountProperty(), pendingCallsCount -> pendingCallsCount > 0)
                , consumer);
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
