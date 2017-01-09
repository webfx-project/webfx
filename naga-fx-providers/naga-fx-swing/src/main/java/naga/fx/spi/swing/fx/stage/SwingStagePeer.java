package naga.fx.spi.swing.fx.stage;

import naga.fx.naga.tk.StagePeer;
import naga.fx.scene.Scene;
import naga.fx.spi.swing.fx.SwingScenePeer;
import naga.fx.stage.*;
import naga.fx.stage.Window;
import naga.fx.sun.tk.TKStageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Bruno Salmon
 */
public class SwingStagePeer implements StagePeer {

    private final Stage stage;
    private final JFrame frame;

    public SwingStagePeer(Stage stage) {
        this(stage, new JFrame());
    }

    private SwingStagePeer(Stage stage, JFrame frame) {
        this.stage = stage;
        this.frame = frame;
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                stage.setWidth((double) frame.getContentPane().getWidth());
                stage.setHeight((double) frame.getContentPane().getHeight());
            }
        });
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public Window getWindow() {
        return stage;
    }

    @Override
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    @Override
    public void onSceneRootChanged() {
        if (stage.getScene().getRoot() != null)
            setWindowContent(((SwingScenePeer) stage.getScene().impl_getPeer()).getSceneComponent());
    }

    private void setWindowContent(Component rootComponent) {
        Container contentPane = frame.getContentPane();
        boolean firstShown = contentPane.getComponents().length == 0;
        contentPane.removeAll();
        contentPane.add(rootComponent);
        if (firstShown) {
            Scene scene = stage.getScene();
            frame.setSize(scene.getWidth().intValue(), scene.getHeight().intValue());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
