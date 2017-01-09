package naga.fx.spi.swing;

import naga.fx.geometry.Rectangle2D;
import naga.fx.naga.tk.ScenePeer;
import naga.fx.naga.tk.StagePeer;
import naga.fx.naga.tk.WindowPeer;
import naga.fx.scene.Scene;
import naga.fx.spi.Toolkit;
import naga.fx.spi.swing.fx.SwingScenePeer;
import naga.fx.spi.swing.fx.stage.SwingStagePeer;
import naga.fx.stage.Screen;
import naga.fx.stage.Stage;
import naga.fx.stage.Window;

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
        super(SwingScheduler.SINGLETON);
    }

    @Override
    public StagePeer createStagePeer(Stage stage) {
        return new SwingStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new SwingScenePeer(scene);
    }

    @Override
    public Screen getPrimaryScreen() {
        return Screen.from(toRectangle2D(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
    }

    private Rectangle2D toRectangle2D(Dimension d) {
        return new Rectangle2D(0, 0, d.getWidth(), d.getHeight());
    }
}
