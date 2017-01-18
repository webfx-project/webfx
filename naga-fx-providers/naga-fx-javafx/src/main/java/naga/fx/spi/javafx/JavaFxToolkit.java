package naga.fx.spi.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
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

    private ChangeListener<Scene> sceneChangeListener;

    @Override
    public Stage getPrimaryStage() {
        Stage primaryStage = super.getPrimaryStage();
        if (getSceneHook() != null && sceneChangeListener == null)
            primaryStage.sceneProperty().addListener(sceneChangeListener = (observable, oldValue, newValue) -> {
                if (newValue != null)
                    getSceneHook().accept(newValue);
            });
        return primaryStage;
    }

    public static class FxApplication extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            // Activating SVG support
            SvgImageLoaderFactory.install();
            executeReadyRunnables();
        }
    }
}
