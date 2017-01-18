package naga.fx.spi.swing.peer;

import emul.javafx.scene.Scene;
import naga.fx.spi.peer.StagePeer;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import emul.com.sun.javafx.tk.TKStageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Bruno Salmon
 */
public class SwingStagePeer implements StagePeer {

    private final Stage stage;
    private final JFrame frame;
    private TKStageListener listener;

    public SwingStagePeer(Stage stage) {
        this(stage, new JFrame());
    }

    private SwingStagePeer(Stage stage, JFrame frame) {
        this.stage = stage;
        this.frame = frame;
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                if (listener != null)
                    listener.changedLocation(frame.getX(), frame.getY());
            }

            @Override
            public void componentResized(ComponentEvent e) {
                if (listener != null)
                    listener.changedSize((float) frame.getWidth(), (float) frame.getHeight());
                ((SwingScenePeer) stage.getScene().impl_getPeer()).changedWindowSize(frame.getWidth() - (float) stagePaddingWidth(), frame.getHeight() - (float) stagePaddingHeight());
            }
        });
    }

    @Override
    public void setTKStageListener(TKStageListener listener) {
        this.listener = listener;
    }

    @Override
    public void setBounds(float x, float y, boolean xSet, boolean ySet, float w, float h, float cw, float ch, float xGravity, float yGravity) {
        //System.out.println("x = " + x +", y = " + y + ", w = " + w + ", h = " + h + ", cw = " + cw + ", ch = " + ch);
        if (xSet || ySet)
            frame.setLocation(xSet ? (int) x : frame.getX(), ySet ? (int) y : frame.getY());
        if (w < 0 && cw > 0)
            w = cw + (float) stagePaddingWidth();
        if (h < 0 && ch > 0)
            h = ch + (float) stagePaddingHeight();
        if (w > 0 || h > 0)
            frame.setSize(w > 0 ? (int) w : frame.getWidth(), h > 0 ? (int) h : frame.getHeight());
    }

    private double stagePaddingWidth() {
        return frame.getWidth() - frame.getContentPane().getWidth();
    }

    private double stagePaddingHeight() {
        return frame.getHeight() - frame.getContentPane().getHeight();
    }

    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
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
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    emul.javafx.stage.WindowEvent we = null; // new emul.javafx.stage.WindowEvent(stage, emul.javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST);
                    emul.javafx.event.Event.fireEvent(stage, we);
                    if (we.isConsumed() && stage == naga.fx.spi.Toolkit.get().getPrimaryStage())
                        System.exit(0);
                }
            });
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
