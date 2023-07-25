package dev.webfx.kit.statusbar.spi;

import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public interface StatusBarProvider {

    boolean hasStatusBar();

    boolean setColor(Color color);

}
