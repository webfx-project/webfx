package naga.providers.toolkit.swing;

import naga.providers.toolkit.swing.fx.SwingDrawingNode;
import naga.providers.toolkit.swing.nodes.layouts.SwingWindow;
import naga.toolkit.fx.spi.DrawingNode;
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
        super(SwingScheduler.SINGLETON, SwingWindow::new);
        registerNodeFactory(DrawingNode.class, SwingDrawingNode::new);
    }
}
