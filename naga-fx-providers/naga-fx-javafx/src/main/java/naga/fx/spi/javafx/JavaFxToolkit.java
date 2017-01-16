package naga.fx.spi.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import javafx.geometry.Rectangle2D;
import naga.fx.spi.peer.ScenePeer;
import naga.fx.spi.Toolkit;
import naga.fx.spi.javafx.peer.FxScenePeer;
import naga.fx.spi.javafx.peer.FxStagePeer;
import naga.fx.stage.Screen;
import naga.fx.spi.peer.StagePeer;
import naga.fx.stage.Window;
import naga.fx.spi.peer.WindowPeer;

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
        javafx.stage.Screen fxPrimaryScreen = javafx.stage.Screen.getPrimary();
        return Screen.from(toRectangle2D(fxPrimaryScreen.getBounds()), toRectangle2D(fxPrimaryScreen.getVisualBounds()));
    }

    private Rectangle2D toRectangle2D(javafx.geometry.Rectangle2D r) {
        return new Rectangle2D(r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
    }

    @Override
    public StagePeer createStagePeer(naga.fx.stage.Stage stage) {
        if (stage != getPrimaryStage())
            return createFxStagePeer(stage, new Stage());
        synchronized (FxApplication.class) {
            return FxApplication.fxStagePeer = createFxStagePeer(stage, FxApplication.fxPrimaryStage);
        }
    }

    protected FxStagePeer createFxStagePeer(naga.fx.stage.Stage stage, Stage fxStage) {
        return new FxStagePeer(stage, fxStage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(naga.fx.scene.Scene scene) {
        return new FxScenePeer(scene);
    }

    public static class FxApplication extends Application {
        public static Stage fxPrimaryStage;
        private static FxStagePeer fxStagePeer;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Activating SVG support
            SvgImageLoaderFactory.install();
            synchronized (FxApplication.class) {
                FxApplication.fxPrimaryStage = primaryStage;
                if (fxStagePeer != null)
                    fxStagePeer.setFxStage(primaryStage);
            }
            executeReadyRunnables();
        }
    }
}
