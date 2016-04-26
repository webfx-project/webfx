package naga.core.spi.gui.swing;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.gui.swing.nodes.*;
import naga.core.spi.platform.Scheduler;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingToolkit  extends GuiToolkit {

    static {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored) {}
    }

    public SwingToolkit() {
        registerNodeFactory(BorderPane.class, SwingBorderPane::new);
        registerNodeFactory(Table.class, SwingTable::new);
        registerNodeFactory(CheckBox.class, SwingCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, SwingCheckBox::new);
        registerNodeFactory(SearchBox.class, SwingSearchBox::new);
    }

    private SwingWindow applicationWindow;
    @Override
    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = new SwingWindow();
        return applicationWindow;
    }

    @Override
    public Scheduler scheduler() {
        return SwingScheduler.SINGLETON;
    }
}
