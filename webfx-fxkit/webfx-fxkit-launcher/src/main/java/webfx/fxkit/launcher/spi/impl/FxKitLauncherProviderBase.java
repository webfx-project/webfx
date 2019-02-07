package webfx.fxkit.launcher.spi.impl;

import com.sun.javafx.application.ParametersImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import webfx.fxkit.launcher.spi.FxKitLauncherProvider;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.function.Factory;

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

    @Override
    public void launchApplication(Factory<Application> applicationFactory, String... args) {
        Application application = applicationFactory.create();
        if (application != null)
            try {
                ParametersImpl.registerParameters(application, new ParametersImpl(args));
                application.init();
                application.start(getPrimaryStage());
            } catch (Exception e) {
                Logger.log("Error while launching the JavaFx application", e);
            }
    }
}
