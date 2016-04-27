package naga.core.spi.gui.cn1;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.cn1.nodes.*;
import naga.core.spi.gui.nodes.*;


/**
 * @author Bruno Salmon
 */
public class CodenameOneToolkit extends GuiToolkit {

    public static void register() {
        register(new CodenameOneToolkit());
    }

    public CodenameOneToolkit() {
        super(Cn1Scheduler.SINGLETON);
        registerNodeFactory(BorderPane.class, Cn1BorderPane::new);
        registerNodeFactory(Table.class, Cn1Table::new);
        registerNodeFactory(CheckBox.class, Cn1CheckBox::new);
        registerNodeFactory(ToggleSwitch.class, Cn1CheckBox::new);
        registerNodeFactory(SearchBox.class, Cn1SearchBox::new);
    }

    private Cn1Window applicationWindow;
    @Override
    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = new Cn1Window();
        return applicationWindow;
    }
}
