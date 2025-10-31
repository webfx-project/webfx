package dev.webfx.kit.launcher;

import dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.service.SingleServiceProvider;
import dev.webfx.platform.util.function.Factory;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebFxKitLauncher {

    public static synchronized WebFxKitLauncherProvider getProvider() {
        return SingleServiceProvider.getProvider(WebFxKitLauncherProvider.class, () -> ServiceLoader.load(WebFxKitLauncherProvider.class));
    }

    public static void launchApplication(Factory<Application> applicationFactory, String... args) {
        WebFxKitLauncherProvider provider = null;
        try {
            provider = getProvider();
        } catch (Exception e) {
            Console.log("No FxKitLauncher provider (assuming server side), JavaFX application will not be launched");
        }
        if (provider != null)
            provider.launchApplication(applicationFactory, args);
    }

    public static boolean isReady() {
        return getProvider().isReady();
    }

    public static void onReady(Runnable runnable) {
        getProvider().onReady(runnable);
    }

    public static Application getApplication() {
        return getProvider().getApplication();
    }

    public static Stage getPrimaryStage() {
        return getProvider().getPrimaryStage();
    }

    public static double getVerticalScrollbarExtraWidth() {
        return getProvider().getVerticalScrollbarExtraWidth();
    }

    public static boolean supportsSvgImageFormat() {
        return getProvider().supportsSvgImageFormat();
    }

    public static boolean supportsWebPImageFormat() {
        return getProvider().supportsWebPImageFormat();
    }

    public static Canvas createWillReadFrequentlyCanvas() {
        return createCanvas( true);
    }

    public static Canvas createWillReadFrequentlyCanvas(double width, double height) {
        return createCanvas(width, height, true);
    }

    public static Canvas createCanvas(boolean willReadFrequently) {
        return createCanvas(0, 0, willReadFrequently);
    }

    public static Canvas createCanvas(double width, double height, boolean willReadFrequently) {
        Canvas canvas = new Canvas(width, height);
        getGraphicsContext2D(canvas, willReadFrequently);
        return canvas;
    }

    public static GraphicsContext getGraphicsContext2D(Canvas canvas, boolean willReadFrequently) {
        return getProvider().getGraphicsContext2D(canvas, willReadFrequently);
    }

    // Canvas HDPI management.
    // Pixel density initially equals to Screen.outputScale, but then can be changed (ex: 1 to get a low-res canvas).

    public static void setCanvasPixelDensity(Canvas canvas, double pixelDensity) {
        getProvider().setCanvasPixelDensity(canvas, pixelDensity);
    }

    public static double getCanvasPixelDensity(Canvas canvas) {
        return canvas == null ? getDefaultCanvasPixelDensity() : getProvider().getCanvasPixelDensity(canvas);
    }

    public static double getDefaultCanvasPixelDensity() {
        return getProvider().getDefaultCanvasPixelDensity();
    }


    public static Bounds measureText(String text, Font font) {
        return getProvider().measureText(text, font);
    }

    public static double measureBaselineOffset(Font font) {
        return getProvider().measureBaselineOffset(font);
    }

    public static ObservableList<Font> loadingFonts() {
        return getProvider().loadingFonts();
    }

    public static String getWebFxCssResourcePath(String webFxCssPath) {
        // If webfxCssPath specifies the "css:" protocol, we remove it before going further,
        if (webFxCssPath.startsWith("css:"))
            webFxCssPath = webFxCssPath.substring(4);
        // otherwise, if it specifies another protocol (ex: "https:"), we return it untouched
        else if (webFxCssPath.contains(":"))
            return webFxCssPath;
        // At this point it should be a CSS file located under the CSS folder in the resources
        // We remove the possible head / to ensure it's now a relative path to that CSS folder
        if (webFxCssPath.startsWith("/"))
            webFxCssPath = webFxCssPath.substring(1);
        // We resolve the relative path from the CSS resource folder
        return "dev/webfx/kit/css/" + webFxCssPath;
    }

    public static ReadOnlyObjectProperty<Insets> safeAreaInsetsProperty() {
        return getProvider().safeAreaInsetsProperty();
    }

    public static Insets getSafeAreaInsets() {
        return getProvider().getSafeAreaInsets();
    }

    public static boolean isFullscreenEnabled() {
        return getProvider().isFullscreenEnabled();
    }

    public static boolean requestNodeFullscreen(Node node) {
        return getProvider().requestNodeFullscreen(node);
    }

    public static boolean exitFullscreen() {
        return getProvider().exitFullscreen();
    }

    // PWA features

    public static boolean supportsAppInstall() {
        return getProvider().supportsAppInstall();
    }

    public static ReadOnlyBooleanProperty appInstallPromptReadyProperty() {
        return getProvider().appInstallPromptReadyProperty();
    }

    public static boolean isAppInstallPromptReady() {
        return appInstallPromptReadyProperty().get();
    }

    public static void promptAppInstall() {
        getProvider().promptAppInstall();
    }

    public static ReadOnlyBooleanProperty appInstalledProperty() {
        return getProvider().appInstalledProperty();
    }

    public static boolean isAppInstalled() {
        return appInstalledProperty().get();
    }

    public static boolean isRunningAsInstalledApp() {
        return getProvider().isRunningAsInstalledApp();
    }

}
