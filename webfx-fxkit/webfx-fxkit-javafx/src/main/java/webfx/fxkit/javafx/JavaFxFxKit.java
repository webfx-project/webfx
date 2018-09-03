package webfx.fxkit.javafx;

import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.fxkits.core.spi.FxKit;
import webfx.platforms.core.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class JavaFxFxKit extends FxKit {

    private static Consumer<Scene> sceneHook;
    private static List<Runnable> readyRunnables = new ArrayList<>();
    private static Stage startingStage;

    public static void registerStartingStage(Stage startingStage) {
        JavaFxFxKit.startingStage = startingStage;
    }

    public JavaFxFxKit() {
        super(FxSchedulerProviderImpl.SINGLETON, startingStage);
        if (startingStage != null) {
            getPrimaryStage();
            onJavaFxPlatformReady();
        } else {
            new Thread(() -> {
                Application.launch(FxApplication.class, "JavaFxFxKit-Launcher");
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
        synchronized (JavaFxFxKit.class) {
            if (readyRunnables != null)
                readyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (JavaFxFxKit.class) {
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
        JavaFxFxKit.sceneHook = sceneHook;
        ((JavaFxFxKit) get()).applySceneHookToPrimaryStage();
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
