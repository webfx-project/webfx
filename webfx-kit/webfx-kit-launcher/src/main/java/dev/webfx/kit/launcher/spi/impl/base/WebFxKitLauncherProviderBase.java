package dev.webfx.kit.launcher.spi.impl.base;

import javafx.stage.Stage;
import dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider;

/**
 * @author Bruno Salmon
 */
public abstract class WebFxKitLauncherProviderBase implements WebFxKitLauncherProvider {

    private final String userAgent;
    private final boolean stageProgrammaticallyRelocatableAndResizable;
    private Stage primaryStage;

    public WebFxKitLauncherProviderBase(String userAgent) {
        this(userAgent, false);
    }

    public WebFxKitLauncherProviderBase(String userAgent, boolean stageProgrammaticallyRelocatableAndResizable) {
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
