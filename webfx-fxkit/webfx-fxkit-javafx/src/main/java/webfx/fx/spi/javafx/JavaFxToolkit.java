package webfx.fx.spi.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.util.function.Consumer;
import webfx.fx.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class JavaFxToolkit extends Toolkit {

    private static Consumer<Scene> sceneHook;
    private static List<Runnable> readyRunnables = new ArrayList<>();
    private static Stage startingStage;

    public static void registerStartingStage(Stage startingStage) {
        JavaFxToolkit.startingStage = startingStage;
    }

    public JavaFxToolkit() {
        super(FxSchedulerProviderImpl.SINGLETON, startingStage);
        if (startingStage != null) {
            getPrimaryStage();
            onJavaFxPlatformReady();
        } else {
            new Thread(() -> {
                Application.launch(FxApplication.class, "JavaFxToolkit-Launcher");
                System.exit(0);
            }).start();
        }
    }

    @Override
    public String getUserAgent() {
        return "JavaFx";
    }

    private static void onJavaFxPlatformReady() {
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
                //runnables.forEach(Runnable::run); doesn't work on Android
                for (Runnable runnable : runnables)
                    runnable.run();
            }
        }
    }

    public static void setSceneHook(Consumer<Scene> sceneHook) {
        JavaFxToolkit.sceneHook = sceneHook;
        ((JavaFxToolkit) get()).applySceneHookToPrimaryStage();
    }

    private void applySceneHookToPrimaryStage() {
        if (sceneChangeListener != null)
            sceneChangeListener.changed(null, null, getPrimaryStage().getScene());
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
        if (sceneChangeListener == null) {
            primaryStage.sceneProperty().addListener(sceneChangeListener = (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Consumer<Scene> sceneHook = getSceneHook();
                    if (sceneHook != null)
                        sceneHook.accept(newValue);
                }
            });
            applySceneHookToPrimaryStage();
        }
        return primaryStage;
    }

    public static class FxApplication extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            onJavaFxPlatformReady();
        }
    }
}
