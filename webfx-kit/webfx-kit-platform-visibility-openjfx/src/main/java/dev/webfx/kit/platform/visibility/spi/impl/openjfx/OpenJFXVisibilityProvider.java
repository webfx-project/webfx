package dev.webfx.kit.platform.visibility.spi.impl.openjfx;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.platform.visibility.VisibilityState;
import dev.webfx.platform.visibility.spi.impl.VisibilityProviderBase;
import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public class OpenJFXVisibilityProvider extends VisibilityProviderBase {

    private Stage primaryStage;
    private final ChangeListener<Boolean> showingPropertyListener = (observable, oldValue, newValue) -> fireVisibilityChanged();

    public OpenJFXVisibilityProvider() {
        // Note: for now, we assume that the primary stage won't change during the application lifecycle
        WebFxKitLauncher.onReady(() -> setPrimaryStage(WebFxKitLauncher.getPrimaryStage()));
    }

    private void setPrimaryStage(Stage primaryStage) {
        if (primaryStage != this.primaryStage) {
            VisibilityState previousVisibilityState = getVisibilityState();
            if (this.primaryStage != null)
                this.primaryStage.showingProperty().removeListener(showingPropertyListener);
            this.primaryStage = primaryStage;
            primaryStage.showingProperty().addListener(showingPropertyListener);
            if (getVisibilityState() != previousVisibilityState)
                fireVisibilityChanged();
        }
    }

    @Override
    public VisibilityState getVisibilityState() {
        return primaryStage != null && primaryStage.isShowing() ? VisibilityState.VISIBLE : VisibilityState.HIDDEN;
    }

}
