package dev.webfx.kit.launcher.spi.impl.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider;

/**
 * @author Bruno Salmon
 */
public abstract class WebFxKitLauncherProviderBase implements WebFxKitLauncherProvider {

    private final boolean stageProgrammaticallyRelocatableAndResizable;
    private Stage primaryStage;

    public WebFxKitLauncherProviderBase(boolean stageProgrammaticallyRelocatableAndResizable) {
        this.stageProgrammaticallyRelocatableAndResizable = stageProgrammaticallyRelocatableAndResizable;
    }

    @Override
    public boolean isStageProgrammaticallyRelocatableAndResizable() {
        return stageProgrammaticallyRelocatableAndResizable;
    }

    @Override
    public Stage getPrimaryStage() {
        if (primaryStage == null) {
            primaryStage = new Stage();
            //primaryStage.impl_setPrimary(true);
        }
        return primaryStage;
    }

    protected final BooleanProperty appInstallPromptReadyProperty = new SimpleBooleanProperty();
    protected final BooleanProperty appInstalledProperty = new SimpleBooleanProperty();

    @Override
    public ReadOnlyBooleanProperty appInstallPromptReadyProperty() {
        return appInstallPromptReadyProperty;
    }

    @Override
    public ReadOnlyBooleanProperty appInstalledProperty() {
        return appInstalledProperty;
    }

}
