package naga.fx.spi.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.fx.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class JavaFxToolkit extends Toolkit {

    private static Consumer<Scene> sceneHook;
    private static List<Runnable> readyRunnables = new ArrayList<>();

    public JavaFxToolkit() {
        super(FxScheduler.SINGLETON);
        new Thread(() -> {
            Application.launch(FxApplication.class, "JavaFxToolkit-Launcher");
            System.exit(0);
        }).start();
    }

    @Override
    public boolean isReady() {
        return readyRunnables == null;
    }

    @Override
    public void onReady(Runnable runnable) {
        synchronized (JavaFxToolkit.class) {
            if (readyRunnables != null)
                readyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (JavaFxToolkit.class) {
            if (readyRunnables != null) {
                List<Runnable> runnables = readyRunnables;
                readyRunnables = null;
                runnables.forEach(Runnable::run);
            }
        }
    }

    public static void setSceneHook(Consumer<Scene> sceneHook) {
        JavaFxToolkit.sceneHook = sceneHook;
    }

    public static Consumer<Scene> getSceneHook() {
        return sceneHook;
    }

    @Override
    public Screen getPrimaryScreen() {
        return Screen.getPrimary();
    }

/*
    @Override
    public StagePeer createStagePeer(javafx.stage.Stage stage) {
        if (stage != getPrimaryStage())
            return createFxStagePeer(stage, new Stage());
        synchronized (FxApplication.class) {
            return FxApplication.fxStagePeer = createFxStagePeer(stage, FxApplication.fxPrimaryStage);
        }
    }

    protected FxStagePeer createFxStagePeer(javafx.stage.Stage stage, Stage fxStage) {
        return new FxStagePeer(stage, fxStage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(javafx.scene.Scene scene) {
        return new FxScenePeer(scene);
    }
*/

    public static class FxApplication extends Application {
        public static Stage fxPrimaryStage;
        //private static FxStagePeer fxStagePeer;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Activating SVG support
            SvgImageLoaderFactory.install();
            synchronized (FxApplication.class) {
                FxApplication.fxPrimaryStage = primaryStage;
/*
                if (fxStagePeer != null)
                    fxStagePeer.setFxStage(primaryStage);
*/
            }
            executeReadyRunnables();
        }
    }
}
