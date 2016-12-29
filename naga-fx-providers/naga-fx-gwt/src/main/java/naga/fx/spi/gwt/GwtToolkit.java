package naga.fx.spi.gwt;

import naga.commons.scheduler.UiScheduler;
import naga.platform.spi.Platform;
import naga.fx.spi.gwt.html.HtmlScene;
import naga.fx.geometry.BoundingBox;
import naga.fx.geometry.Bounds;
import naga.fx.spi.Toolkit;
import naga.fx.stage.Screen;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class GwtToolkit extends Toolkit {

    public GwtToolkit() {
        super(/* TODO: remove this dependency to Platform */(UiScheduler) Platform.get().scheduler(), GwtWindow::new, HtmlScene::new);
    }

    @Override
    public Screen getPrimaryScreen() {
        return new Screen() {
            @Override
            public Bounds getBounds() {
                return toBounds(window.screen.width, window.screen.height);
            }

            @Override
            public Bounds getVisualBounds() {
                return toBounds(window.screen.availWidth, window.screen.availHeight);
            }

            Bounds toBounds(double width, double height) {
                return new BoundingBox(0, 0, width, height);
            }
        };
    }
}