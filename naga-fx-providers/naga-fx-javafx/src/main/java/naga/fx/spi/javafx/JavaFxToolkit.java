package naga.fx.spi.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Factory;
import naga.fx.spi.javafx.fx.FxNodeViewerFactory;
import naga.fx.spi.javafx.fx.FxScene;
import naga.fx.spi.javafx.fx.stage.FxWindow;
import naga.fx.geometry.BoundingBox;
import naga.fx.geometry.Bounds;
import naga.fx.spi.viewer.NodeViewerFactory;
import naga.fx.stage.Screen;
import naga.fx.stage.Window;
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
        this(() -> new FxWindow(FxApplication.primaryStage), FxNodeViewerFactory.SINGLETON);
    }

    protected JavaFxToolkit(Factory<Window> windowFactory, NodeViewerFactory nodeViewerFactory) {
        super(FxScheduler.SINGLETON, windowFactory, () -> new FxScene(nodeViewerFactory));
        new Thread(() -> Application.launch(FxApplication.class), "JavaFxToolkit-Launcher").start();
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
        return new Screen() {
            @Override
            public Bounds getBounds() {
                return toBounds(javafx.stage.Screen.getPrimary().getBounds());
            }

            @Override
            public Bounds getVisualBounds() {
                return toBounds(javafx.stage.Screen.getPrimary().getVisualBounds());
            }

            private Bounds toBounds(Rectangle2D r) {
                return new BoundingBox(r.getMinX(), r.getMinY(), 0, r.getWidth(), r.getHeight(), 0);
            }
        };
    }

    @Override
    public Window getPrimaryWindow() {
        synchronized (FxApplication.class) {
            return FxApplication.fxWindow = (FxWindow) super.getPrimaryWindow();
        }
    }

    public static class FxApplication extends Application {
        public static Stage primaryStage;
        private static FxWindow fxWindow;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Activating SVG support
            SvgImageLoaderFactory.install();
            synchronized (FxApplication.class) {
                FxApplication.primaryStage = primaryStage;
                primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
                if (fxWindow != null)
                    fxWindow.setStage(primaryStage);
            }
            executeReadyRunnables();
        }
    }
}
