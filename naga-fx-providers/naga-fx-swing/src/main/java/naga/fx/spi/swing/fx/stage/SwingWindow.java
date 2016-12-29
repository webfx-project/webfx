package naga.fx.spi.swing.fx.stage;

import naga.fx.spi.swing.fx.SwingScene;
import naga.fx.stage.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Bruno Salmon
 */
public class SwingWindow extends Window {

    private final JFrame frame;

    public SwingWindow() {
        this(new JFrame());
    }

    private SwingWindow(JFrame frame) {
        this.frame = frame;
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setWidth((double) frame.getContentPane().getWidth());
                setHeight((double) frame.getContentPane().getHeight());
            }
        });
    }

    @Override
    protected void onTitleUpdate() {
        frame.setTitle(getTitle());
    }

    @Override
    protected void onSceneRootUpdate() {
        if (getScene().getRoot() != null)
            setWindowContent(((SwingScene) getScene()).getSceneComponent());
    }

    private void setWindowContent(Component rootComponent) {
        Container contentPane = frame.getContentPane();
        boolean firstShown = contentPane.getComponents().length == 0;
        contentPane.removeAll();
        contentPane.add(rootComponent);
        if (firstShown) {
            frame.setSize(getScene().getWidth().intValue(), getScene().getHeight().intValue());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
