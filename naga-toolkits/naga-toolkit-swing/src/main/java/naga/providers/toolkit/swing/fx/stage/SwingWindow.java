package naga.providers.toolkit.swing.fx.stage;

import naga.providers.toolkit.swing.fx.SwingScene;
import naga.toolkit.fx.scene.impl.WindowImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Bruno Salmon
 */
public class SwingWindow extends WindowImpl {

    private final JFrame frame;

    public SwingWindow() {
        this(new JFrame());
    }

    SwingWindow(JFrame frame) {
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
        setWindowContent(((SwingScene) getScene()).getSceneComponent());
    }

    private void setWindowContent(Component rootComponent) {
        Container contentPane = frame.getContentPane();
        boolean firstShown = contentPane.getComponents().length == 0;
        contentPane.removeAll();
        contentPane.add(rootComponent);
        if (firstShown) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize((int) (screenSize.getWidth() * 0.8), (int) (screenSize.getHeight() * 0.9));
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
