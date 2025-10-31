package dev.webfx.kit.launcher.spi;

import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.function.Factory;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.Clipboard;
import javafx.scene.input.Dragboard;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public interface WebFxKitLauncherProvider {

    HostServices getHostServices();

    boolean isStageProgrammaticallyRelocatableAndResizable();

    default Screen getPrimaryScreen() {
        return Screen.getPrimary();
    }

    default Clipboard getSystemClipboard() {
        return Clipboard.getSystemClipboard();
    }

    default Dragboard createDragboard(Scene scene) {
        return null;
    }

    Stage getPrimaryStage();

    Application getApplication();

    void launchApplication(Factory<Application> applicationFactory, String... args);

    default boolean isReady() {
        return true;
    }

    default void onReady(Runnable runnable) {
        UiScheduler.runInUiThread(runnable);
    }

    default double getVerticalScrollbarExtraWidth() {
        return 0;
    }

    default boolean supportsSvgImageFormat() { return false; }

    default boolean supportsWebPImageFormat() { return false; }

    default GraphicsContext getGraphicsContext2D(Canvas canvas, boolean willReadFrequently) {
        return canvas.getGraphicsContext2D();
    }

    default DoubleProperty canvasPixelDensityProperty(Canvas canvas) {
        return null;
    }

    default void setCanvasPixelDensity(Canvas canvas, double pixelDensity) {
        DoubleProperty pixelDensityProperty = canvasPixelDensityProperty(canvas);
        if (pixelDensityProperty != null)
            pixelDensityProperty.set(pixelDensity);
    }

    default double getCanvasPixelDensity(Canvas canvas) {
        DoubleProperty pixelDensityProperty = canvasPixelDensityProperty(canvas);
        if (pixelDensityProperty != null)
            return pixelDensityProperty.doubleValue();
        return getDefaultCanvasPixelDensity();
    }

    default double getDefaultCanvasPixelDensity() {
        return Screen.getPrimary().getOutputScaleX();
    }

    Bounds measureText(String text, Font font);

    double measureBaselineOffset(Font font);

    default ObservableList<Font> loadingFonts() {
        return FXCollections.emptyObservableList(); // Default implementation fpr synchronous font loading toolkits (such as OpenJFX)
    }

    ReadOnlyObjectProperty<Insets> safeAreaInsetsProperty();

    default Insets getSafeAreaInsets() {
        return safeAreaInsetsProperty().get();
    }

    default boolean isFullscreenEnabled() {
        return false;
    }

    default boolean requestNodeFullscreen(Node node) {
        return false;
    }

    default boolean exitFullscreen() {
        return false;
    }

    default boolean supportsAppInstall() {
        return false;
    }

    default ReadOnlyBooleanProperty appInstallPromptReadyProperty() {
        return new SimpleBooleanProperty();
    }

    default void promptAppInstall() {
    }

    default ReadOnlyBooleanProperty appInstalledProperty() {
        return new SimpleBooleanProperty();
    }

    default boolean isRunningAsInstalledApp() {
        return false;
    }

}
