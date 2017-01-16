package naga.fx.spi.swing;

import javafx.geometry.Rectangle2D;
import naga.fx.spi.peer.ScenePeer;
import naga.fx.spi.peer.StagePeer;
import naga.fx.spi.peer.WindowPeer;
import naga.fx.scene.Scene;
import naga.fx.spi.Toolkit;
import naga.fx.spi.swing.peer.SwingScenePeer;
import naga.fx.spi.swing.peer.SwingStagePeer;
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
