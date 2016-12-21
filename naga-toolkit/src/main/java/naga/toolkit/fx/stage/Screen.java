package naga.toolkit.fx.stage;

import naga.toolkit.fx.geometry.Bounds;
import naga.toolkit.fx.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public interface Screen {

    Bounds getBounds();

    Bounds getVisualBounds();

    static Screen getPrimary() {
        return Toolkit.get().getPrimaryScreen();
    }
}
