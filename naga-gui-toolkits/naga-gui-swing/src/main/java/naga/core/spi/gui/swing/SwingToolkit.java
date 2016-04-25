package naga.core.spi.gui.swing;

import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.gui.swing.nodes.SwingBorderPane;
import naga.core.spi.gui.swing.nodes.SwingCheckBox;
import naga.core.spi.gui.swing.nodes.SwingSearchBox;
import naga.core.spi.gui.swing.nodes.SwingTable;
import naga.core.spi.platform.Scheduler;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingToolkit  extends GuiToolkit {

    public SwingToolkit() {
        registerNodeFactory(BorderPane.class, SwingBorderPane::new);
        registerNodeFactory(Table.class, SwingTable::new);
        registerNodeFactory(CheckBox.class, SwingCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, SwingCheckBox::new);
        registerNodeFactory(SearchBox.class, SwingSearchBox::new);
    }

    private GuiNode dontGarbageRootNode; // keeping reference to avoid garbage collection
    private JFrame appFrame;
    @Override
    public void displayRootNode(GuiNode rootNode) {
        dontGarbageRootNode = rootNode;
        if (appFrame == null) {
            try {
                UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch(Exception ignored) {}
            appFrame = new JFrame();
            appFrame.setSize(800, 600);
            appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        appFrame.getContentPane().add((Component) rootNode.unwrapToToolkitNode());
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }

    @Override
    public Scheduler scheduler() {
        return SwingScheduler.SINGLETON;
    }
}
