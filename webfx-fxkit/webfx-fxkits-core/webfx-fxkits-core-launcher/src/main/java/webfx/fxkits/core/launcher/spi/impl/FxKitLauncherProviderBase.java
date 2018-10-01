package webfx.fxkits.core.launcher.spi.impl;

import com.sun.javafx.application.ParametersImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import webfx.fxkits.core.launcher.spi.FxKitLauncherProvider;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public abstract class FxKitLauncherProviderBase implements FxKitLauncherProvider {

    private Stage primaryStage;

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
