package dev.webfx.kit.statusbar.spi.impl.none;

import dev.webfx.kit.statusbar.spi.StatusBarProvider;
import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public final class NoneStatusBarProvide implements StatusBarProvider {

    @Override
    public boolean hasStatusBar() {
        return false;
    }

    @Override
    public boolean setColor(Color color) {
        return false;
    }
}
