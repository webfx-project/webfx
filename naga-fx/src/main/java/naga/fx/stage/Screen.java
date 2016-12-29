package naga.fx.stage;

import naga.fx.geometry.Bounds;
import naga.fx.spi.Toolkit;

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
