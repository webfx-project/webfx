package naga.providers.toolkit.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;
import naga.commons.util.function.Consumer;
import naga.commons.util.function.Factory;
import naga.providers.toolkit.javafx.fx.FxDrawingNode;
import naga.providers.toolkit.javafx.nodes.layouts.FxWindow;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplayResultSetBuilder;
import naga.toolkit.fx.spi.DrawingNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.layouts.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class JavaFxToolkit extends Toolkit {

    private static Consumer<Scene> sceneHook;
    private static List<Runnable> readyRunnables = new ArrayList<>();

    public JavaFxToolkit() {
        this(() -> new FxWindow(FxApplication.primaryStage));
    }

    protected JavaFxToolkit(Factory<Window> windowFactory) {
        super(FxScheduler.SINGLETON, windowFactory);
        new Thread(() -> Application.launch(FxApplication.class), "JavaFxToolkit-Launcher").start();
        registerNodeFactory(DrawingNode.class, FxDrawingNode::new);
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

    public static DisplayResultSet transformDisplayResultSetValuesToProperties(DisplayResultSet rs) {
        return DisplayResultSetBuilder.convertDisplayResultSet(rs, SimpleObjectProperty::new);
    }

    @Override
    public Window getApplicationWindow() {
        synchronized (FxApplication.class) {
            return FxApplication.fxApplicationWindow = (FxWindow) super.getApplicationWindow();
        }
    }

    public static class FxApplication extends Application {
        public static Stage primaryStage;
        private static FxWindow fxApplicationWindow;

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Activating SVG support
            SvgImageLoaderFactory.install();
            synchronized (FxApplication.class) {
                FxApplication.primaryStage = primaryStage;
                primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
                if (fxApplicationWindow != null)
                    fxApplicationWindow.setStage(primaryStage);
            }
            executeReadyRunnables();
        }
    }
}
