package naga.providers.toolkit.swing;

import naga.providers.toolkit.swing.fx.SwingScene;
import naga.providers.toolkit.swing.fx.stage.SwingWindow;
import naga.toolkit.spi.Toolkit;

import javax.swing.*;

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
}
