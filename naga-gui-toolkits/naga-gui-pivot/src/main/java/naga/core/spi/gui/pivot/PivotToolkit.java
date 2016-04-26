package naga.core.spi.gui.pivot;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.gui.pivot.nodes.*;
import naga.core.spi.platform.Scheduler;


/**
 * @author Bruno Salmon
 */
public class PivotToolkit extends GuiToolkit {

    public PivotToolkit() {
        registerNodeFactory(BorderPane.class, PivotBorderPane::new);
        registerNodeFactory(Table.class, PivotTable::new);
        registerNodeFactory(CheckBox.class, PivotCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, PivotCheckBox::new);
        registerNodeFactory(SearchBox.class, PivotSearchBox::new);
    }

    private PivotWindow applicationWindow;
    @Override
    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = new PivotWindow();
        return applicationWindow;
    }

    @Override
    public Scheduler scheduler() {
        return PivotScheduler.SINGLETON;
    }
}
