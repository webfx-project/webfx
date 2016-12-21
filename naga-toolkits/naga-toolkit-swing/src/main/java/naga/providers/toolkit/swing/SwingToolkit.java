package naga.providers.toolkit.swing;

import naga.providers.toolkit.swing.fx.SwingScene;
import naga.providers.toolkit.swing.fx.stage.SwingWindow;
import naga.toolkit.fx.geometry.BoundingBox;
import naga.toolkit.fx.geometry.Bounds;
import naga.toolkit.fx.stage.Screen;
import naga.toolkit.fx.spi.Toolkit;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingToolkit extends Toolkit {

    static {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored) {}
    }

    public SwingToolkit() {
        super(SwingScheduler.SINGLETON, SwingWindow::new, SwingScene::new);
    }

    @Override
    public Screen getPrimaryScreen() {
        return new Screen() {

            @Override
            public Bounds getBounds() {
                return toBounds(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
            }

            @Override
            public Bounds getVisualBounds() {
                return getBounds(); // not exact but enough for now
            }

            private Bounds toBounds(Dimension d) {
                return new BoundingBox(0, 0, 0, d.getWidth(), d.getHeight(), 0);
            }
        };

    }
}
