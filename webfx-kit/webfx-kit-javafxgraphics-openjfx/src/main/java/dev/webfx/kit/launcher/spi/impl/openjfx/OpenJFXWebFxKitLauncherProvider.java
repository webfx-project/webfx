package dev.webfx.kit.launcher.spi.impl.openjfx;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.launcher.spi.impl.base.WebFxKitLauncherProviderBase;
import dev.webfx.kit.util.properties.FXProperties;
import dev.webfx.kit.util.properties.Unregisterable;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.util.function.Factory;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class OpenJFXWebFxKitLauncherProvider extends WebFxKitLauncherProviderBase {

    private static List<Runnable> onReadyRunnables = new ArrayList<>();
    private static Factory<Application> applicationFactory;

    private static Stage primaryStage;
    private static Application application;

    public OpenJFXWebFxKitLauncherProvider() {
        super(true);
    }

    @Override
    public double getVerticalScrollbarExtraWidth() {
        // OpenJFX has a 15px bar width on desktops, but Gluon provides a perfect scrollbar on mobiles (not introducing extra space)
        return OperatingSystem.isMobile() ? 0 : 15;
    }

    @Override
    public HostServices getHostServices() {
        return getApplication().getHostServices();
    }

    @Override
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        OpenJFXWebFxKitLauncherProvider.applicationFactory = applicationFactory;
        new Thread(() -> {
            Application.launch(FxKitWrapperApplication.class, args);
            System.exit(0);
        }).start();
    }

    private static void onJavaFxToolkitReady() {
        // Activating SVG support
        // de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory.install();
        executeReadyRunnables();
    }

    @Override
    public boolean isReady() {
        return onReadyRunnables == null;
    }

    @Override
    public void onReady(Runnable runnable) {
        synchronized (OpenJFXWebFxKitLauncherProvider.class) {
            if (onReadyRunnables != null)
                onReadyRunnables.add(runnable);
            else
                super.onReady(runnable);
        }
    }

    private static void executeReadyRunnables() {
        synchronized (OpenJFXWebFxKitLauncherProvider.class) {
            if (onReadyRunnables != null) {
                List<Runnable> runnables = onReadyRunnables;
                onReadyRunnables = null;
                //runnables.forEach(Runnable::run); doesn't work on Android
                for (Runnable runnable : runnables)
                    runnable.run();
            }
        }
    }

    public static class FxKitWrapperApplication extends Application {

        @Override
        public void init() throws Exception {
            if (applicationFactory != null)
                application = applicationFactory.create();
            if (application != null) {
                //ParametersImpl.registerParameters(application, getParameters());
                application.init();
            }
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            OpenJFXWebFxKitLauncherProvider.primaryStage = primaryStage;
            onJavaFxToolkitReady();
            if (application != null) {
                // WebFX CSS: automatically adding the main.css style sheet to the scene when added to the stage
                primaryStage.sceneProperty().addListener((observable, oldValue, newScene) -> {
                    if (newScene != null) {
                        String webFxCssResourcePath = WebFxKitLauncher.getWebFxCssResourcePath("main.css");
                        if (!newScene.getStylesheets().contains(webFxCssResourcePath))
                            newScene.getStylesheets().add(0, webFxCssResourcePath);
                    }
                });
                application.start(primaryStage);
            }
        }
    }

    private final Text measurementText = new Text();
    @Override
    public Bounds measureText(String text, Font font) {
        measurementText.setText(text);
        measurementText.setFont(font);
        return measurementText.getLayoutBounds();
    }

    @Override
    public double measureBaselineOffset(Font font) {
        measurementText.setText("Baseline text");
        measurementText.setFont(font);
        return measurementText.getBaselineOffset();
    }

    private final ReadOnlyObjectProperty<Insets> safeAreaInsetsProperty = new SimpleObjectProperty<>(Insets.EMPTY);

    @Override
    public ReadOnlyObjectProperty<Insets> safeAreaInsetsProperty() {
        return safeAreaInsetsProperty;
    }

    @Override
    public boolean isFullscreenEnabled() {
        return true; // always enabled in OpenJFX
    }

    private Node fullscreenNode;
    private Pane fullscreenNodePreviousParent;
    private int fullscreenNodePreviousChildIndex;
    private Scene fullscreenPreviousScene;
    private Unregisterable fullscreenExitListener;

    @Override
    public boolean requestNodeFullscreen(Node node) {
        if (fullscreenNode == node && primaryStage.isFullScreen()) // already fullscreen
            return true;
        if (node == null || !(node.getParent() instanceof Pane)) {
            Console.warn("requestNodeFullscreen() is supported only for nodes inside a Pane (or derived classes)");
            return false;
        }
        moveBackFullscreenNode();
        fullscreenNode = node;
        fullscreenNodePreviousParent = (Pane) node.getParent();
        fullscreenNodePreviousChildIndex = fullscreenNodePreviousParent.getChildren().indexOf(node);
        fullscreenNodePreviousParent.getChildren().remove(fullscreenNode);
        fullscreenPreviousScene = primaryStage.getScene();
        primaryStage.setScene(new Scene(new BorderPane(node)));
        primaryStage.setFullScreen(true);
        fullscreenExitListener = FXProperties.runOnPropertyChange(fullscreen -> {
            if (!fullscreen) {
                moveBackFullscreenNode();
                primaryStage.setScene(fullscreenPreviousScene);
                fullscreenExitListener.unregister();
            }
        }, primaryStage.fullScreenProperty());
        return true;
    }

    @Override
    public boolean exitFullscreen() {
        if (fullscreenNode == null)
            return false;
        primaryStage.setFullScreen(false);
        return true;
    }

    private void moveBackFullscreenNode() {
        if (fullscreenNode != null) {
            fullscreenNodePreviousParent.getChildren().add(fullscreenNodePreviousChildIndex, fullscreenNode);
            fullscreenNode = null;
        }
    }
}
