package dev.webfx.kit.launcher.spi;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.Dragboard;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import dev.webfx.platform.uischeduler.UiScheduler;
import dev.webfx.platform.util.function.Factory;

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

    default boolean supportsWebP() { return false; }

    FastPixelReaderWriter getFastPixelReaderWriter(Image image);

    default GraphicsContext getGraphicsContext2D(Canvas canvas, boolean willReadFrequently) {
        return canvas.getGraphicsContext2D();
    }

    Bounds measureText(String text, Font font);

    double measureBaselineOffset(Font font);

    default ObservableList<Font> loadingFonts() {
        return FXCollections.emptyObservableList(); // Default implementation fpr synchronous font loading toolkits (such as OpenJFX)
    }
}
