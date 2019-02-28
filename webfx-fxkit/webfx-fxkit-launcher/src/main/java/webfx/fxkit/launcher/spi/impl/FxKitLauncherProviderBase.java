package webfx.fxkit.launcher.spi.impl;

import javafx.stage.Stage;
import webfx.fxkit.launcher.spi.FxKitLauncherProvider;

/**
 * @author Bruno Salmon
 */
public abstract class FxKitLauncherProviderBase implements FxKitLauncherProvider {

    private final String userAgent;
    private final boolean stageProgrammaticallyRelocatableAndResizable;
    private Stage primaryStage;

    public FxKitLauncherProviderBase(String userAgent) {
        this(userAgent, false);
    }

    public FxKitLauncherProviderBase(String userAgent, boolean stageProgrammaticallyRelocatableAndResizable) {
        this.userAgent = userAgent;
        this.stageProgrammaticallyRelocatableAndResizable = stageProgrammaticallyRelocatableAndResizable;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
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
