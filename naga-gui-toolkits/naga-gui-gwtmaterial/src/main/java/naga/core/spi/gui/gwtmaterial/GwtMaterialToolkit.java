package naga.core.spi.gui.gwtmaterial;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.gwtmaterial.nodes.*;
import naga.core.spi.gui.nodes.*;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialToolkit extends GuiToolkit {

    public GwtMaterialToolkit() {
        registerNodeFactory(BorderPane.class, GwtBorderPane::new);
        registerNodeFactory(Table.class, GwtTable::new);
        registerNodeFactory(CheckBox.class, GwtMaterialCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, GwtMaterialCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtMaterialSearchBox::new);
    }

    private GwtWindow applicationWindow;
    @Override
    public Window getApplicationWindow() {
        if (applicationWindow == null)
            applicationWindow = new GwtWindow();
        return applicationWindow;
    }
}
