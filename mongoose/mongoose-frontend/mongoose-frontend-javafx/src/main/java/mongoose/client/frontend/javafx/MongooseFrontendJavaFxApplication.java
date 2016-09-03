package mongoose.client.frontend.javafx;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import mongoose.client.frontend.MongooseFrontendApplication;
import naga.framework.activity.client.UiApplicationContext;
import naga.framework.ui.rx.RxScheduler;
import naga.framework.ui.rx.RxUi;
import naga.platform.bus.call.PendingBusCall;
import naga.providers.toolkit.javafx.JavaFxToolkit;
import naga.providers.toolkit.javafx.util.FxImageStore;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;
import rx.Observable;

/**
 * @author Bruno Salmon
 */
public class MongooseFrontendJavaFxApplication {

    public static void main(String[] args) {
        installJavaFxHooks();
        // Once hooks are set, we can start the application
        MongooseFrontendApplication.main(args);
        Observable.combineLatest(
                RxUi.observe(UiApplicationContext.getUiApplicationContext().windowBoundProperty()),
                RxUi.observe(PendingBusCall.pendingCallsCountProperty()),
                (windowBound, pendingCallsCount) -> !windowBound || pendingCallsCount > 0)
                .observeOn(RxScheduler.UI_SCHEDULER)
                .subscribe(MongooseFrontendJavaFxApplication::setLoadingSpinnerVisible);
    }

    private static void installJavaFxHooks() {
        // Setting JavaFx scene hook to apply the mongoose css file
        JavaFxToolkit.setSceneHook(scene -> scene.getStylesheets().addAll("mongoose/client/java/css/mongoose.css"));
    }

    private static ImageView spinner;

    private static void setLoadingSpinnerVisible(boolean visible) {
        GuiNode guiNode = Toolkit.get().getApplicationWindow().getNode();
        if (guiNode != null) {
            Node node = (Node) guiNode.unwrapToNativeNode();
            if (node instanceof Pane) {
                Pane rootPane = (Pane) node;
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
