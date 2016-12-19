package naga.providers.toolkit.html;

import naga.commons.scheduler.UiScheduler;
import naga.platform.spi.Platform;
import naga.providers.toolkit.html.fx.HtmlWindow;
import naga.providers.toolkit.html.fx.html.HtmlScene;
import naga.toolkit.fx.geometry.BoundingBox;
import naga.toolkit.fx.geometry.Bounds;
import naga.toolkit.fx.stage.Screen;
import naga.toolkit.spi.Toolkit;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class HtmlToolkit extends Toolkit {

    public HtmlToolkit() {
        super(/* TODO: remove this dependency to Platform */(UiScheduler) Platform.get().scheduler(), HtmlWindow::new, HtmlScene::new);
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