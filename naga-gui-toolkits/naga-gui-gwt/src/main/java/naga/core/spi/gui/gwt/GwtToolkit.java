package naga.core.spi.gui.gwt;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.gwt.nodes.*;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class GwtToolkit extends GuiToolkit {

    public GwtToolkit() {
        super(Platform.get().scheduler(), GwtWindow::new);
        registerNodeFactory(BorderPane.class, GwtBorderPane::new);
        registerNodeFactory(Table.class, GwtTable::new);
        registerNodeFactory(CheckBox.class, GwtCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, GwtCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtSearchBox::new);
    }
}