package webfx.fxkits.core.spi;

import com.sun.javafx.application.ParametersImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkits.core.spi.peer.ScenePeer;
import webfx.fxkits.core.spi.peer.StagePeer;
import webfx.fxkits.core.spi.peer.WindowPeer;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.uischeduler.UiScheduler;
import webfx.platforms.core.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class FxKitProvider {

    private Stage primaryStage;

    public abstract String getUserAgent();

    public abstract Screen getPrimaryScreen();

    public Stage getPrimaryStage() {
        if (primaryStage == null) {
            primaryStage = new Stage();
            //primaryStage.impl_setPrimary(true);
        }
        return primaryStage;
    }

    public abstract StagePeer createStagePeer(Stage stage);

    public abstract WindowPeer createWindowPeer(Window window);

    public abstract ScenePeer createScenePeer(Scene scene);

    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        Application application = applicationFactory.create();
        if (application != null)
            try {
                ParametersImpl.registerParameters(application, new ParametersImpl(args));
                application.init();
                application.start(getPrimaryStage());
            } catch (Exception e) {
                Logger.log("Error while launching the JavaFx application", e);
            }
    }

    public boolean isReady() {
        return true;
    }

    public void onReady(Runnable runnable) {
        UiScheduler.runInUiThread(runnable);
    }

    public double getVerticalScrollbarExtraWidth() {
        return 16;
    }

}
