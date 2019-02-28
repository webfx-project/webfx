package webfx.fxkit.gwt.launcher;

import com.sun.javafx.application.ParametersImpl;
import elemental2.dom.DomGlobal;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import webfx.fxkit.launcher.spi.impl.FxKitLauncherProviderBase;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.function.Factory;


/**
 * @author Bruno Salmon
 */
public final class GwtFxKitLauncherProvider extends FxKitLauncherProviderBase {

    public GwtFxKitLauncherProvider() {
        super(DomGlobal.navigator.userAgent);
    }

    @Override
    public Screen getPrimaryScreen() {
        elemental2.dom.Screen screen = DomGlobal.screen;
        return Screen.from(toRectangle2D(screen.width, screen.height), toRectangle2D(screen.availWidth, screen.availHeight));
    }

    private static Rectangle2D toRectangle2D(double width, double height) {
        return new Rectangle2D(0, 0, width, height);
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