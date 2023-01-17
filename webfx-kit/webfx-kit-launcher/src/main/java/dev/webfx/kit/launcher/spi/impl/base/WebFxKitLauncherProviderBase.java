package dev.webfx.kit.launcher.spi.impl.base;

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
}
