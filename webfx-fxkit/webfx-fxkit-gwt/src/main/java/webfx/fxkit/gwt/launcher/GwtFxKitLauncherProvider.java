package webfx.fxkit.gwt.launcher;

import elemental2.dom.DomGlobal;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import webfx.fxkit.launcher.spi.impl.FxKitLauncherProviderBase;


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
}