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
import javafx.scene.layout.Region;
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

    /**
     * Launches the JavaFX application, if this kit can launch one. Does nothing by default.
     *
     * <p><b>The default is a no-op because a boot calls this unconditionally.</b>
     * {@link dev.webfx.kit.launcher.WebFxKitLauncherModuleBooter} runs at {@code APPLICATION_LAUNCH_LEVEL} in
     * every WebFX executable and calls it on whatever provider is registered, with a factory that yields the
     * registered {@code Application} service — of which there may be none. A headless or command-line
     * executable has nothing to launch, and that is an ordinary state rather than a misuse: such a provider
     * simply does not override this.
     *
     * <p><b>A provider must not throw from here.</b> {@code APPLICATION_LAUNCH_LEVEL} sits between the jobs
     * initializer and the jobs starter, and {@link dev.webfx.kit.launcher.WebFxKitLauncher#launchApplication}
     * guards only the provider LOOKUP with a try/catch, not this call — so a throw aborts the boot before any
     * application job can start. That is how it was found: a command-line tool's provider raised
     * {@code UnsupportedOperationException} here on the reasoning that reaching it would be a bug, and killed
     * the boot of every command it had.
     *
     * <p>Note that "nothing to launch" was already handled once, per-kit rather than here:
     * {@code Elemental2WebFxKitLauncherProvider} creates from the factory and proceeds only
     * {@code if (application != null)}. Saying it in the interface means the next provider does not have to
     * rediscover it. The OpenJFX provider cannot make that check itself — it stores the factory and invokes it
     * later, on the FX thread once the toolkit is up — which is also why this cannot be checked in
     * {@code WebFxKitLauncher} on the caller's behalf.
     */
    default void launchApplication(Factory<Application> applicationFactory, String... args) {
    }

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

    default void readPaddingFromCSS(Region region, boolean canChange) {
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
