package webfx.fxkit.javafx;

import com.sun.javafx.application.ParametersImpl;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkits.core.spi.FxKitProvider;
import webfx.fxkits.core.spi.peer.ScenePeer;
import webfx.fxkits.core.spi.peer.StagePeer;
import webfx.fxkits.core.spi.peer.WindowPeer;
import webfx.platforms.core.util.function.Factory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class JavaFxFxKitProvider extends FxKitProvider {

    private static List<Runnable> readyRunnables = new ArrayList<>();
    private static Factory<Application> applicationFactory;

    public JavaFxFxKitProvider() {
    }

    @Override
    public StagePeer createStagePeer(Stage stage) {
        return null;
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return null;
    }

    @Override
    public String getUserAgent() {
        return "JavaFx";
    }

    @Override
    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        JavaFxFxKitProvider.applicationFactory = applicationFactory;
        new Thread(() -> {
            Application.launch(FxKitWrapperApplication.class, args);
            System.exit(0);
        }).start();
    }

    private static void onJavaFxToolkitReady() {
        // Activating SVG support
        SvgImageLoaderFactory.install();
        executeReadyRunnables();
    }

    @Override
    public boolean isReady() {
        return readyRunnables == null;
    }

    @Override
    public void onReady(Runnable runnable) {
        synchronized (JavaFxFxKitProvider.class) {
            if (readyRunnables != null)
                readyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (JavaFxFxKitProvider.class) {
            if (readyRunnables != null) {
                List<Runnable> runnables = readyRunnables;
                readyRunnables = null;
                //runnables.forEach(Runnable::run); doesn't work on Android
                for (Runnable runnable : runnables)
                    runnable.run();
            }
        }
    }

    @Override
    public Screen getPrimaryScreen() {
        return Screen.getPrimary();
    }

    public static class FxKitWrapperApplication extends Application {

        Application application;

        @Override
        public void init() throws Exception {
            if (applicationFactory != null)
                application = applicationFactory.create();
            if (application != null) {
                ParametersImpl.registerParameters(application, getParameters());
                application.init();
            }
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            onJavaFxToolkitReady();
            if (application != null)
                application.start(primaryStage);
        }

    }
}
