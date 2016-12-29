package naga.fx.spi.swing;

import naga.fx.spi.swing.fx.SwingScene;
import naga.fx.spi.swing.fx.stage.SwingWindow;
import naga.fx.geometry.BoundingBox;
import naga.fx.geometry.Bounds;
import naga.fx.stage.Screen;
import naga.fx.spi.Toolkit;

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
