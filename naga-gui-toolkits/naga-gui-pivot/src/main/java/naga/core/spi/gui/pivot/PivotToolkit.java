package naga.core.spi.gui.pivot;

import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.gui.pivot.nodes.PivotBorderPane;
import naga.core.spi.gui.pivot.nodes.PivotCheckBox;
import naga.core.spi.gui.pivot.nodes.PivotSearchBox;
import naga.core.spi.gui.pivot.nodes.PivotTable;
import naga.core.spi.platform.Scheduler;
import org.apache.pivot.wtk.*;


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

    private GuiNode dontGarbageRootNode; // keeping reference to avoid garbage collection
    private Window appWindow;
    @Override
    public void displayRootNode(GuiNode rootNode) {
        dontGarbageRootNode = rootNode;
        if (appWindow == null) {
            DesktopApplicationContext.main(Application.Adapter.class, new String[]{});
            appWindow = new Window((Component) rootNode.unwrapToToolkitNode());
            appWindow.setMaximized(true);
            appWindow.open(DesktopApplicationContext.getDisplays().get(0));
        }
        appWindow.setVisible(true);
    }

    @Override
    public Scheduler scheduler() {
        return PivotScheduler.SINGLETON;
    }
}
