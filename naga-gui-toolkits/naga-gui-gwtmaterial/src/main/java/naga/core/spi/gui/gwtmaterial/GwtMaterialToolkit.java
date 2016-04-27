package naga.core.spi.gui.gwtmaterial;

import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.gwtmaterial.nodes.*;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.platform.client.gwt.GwtPlatform;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialToolkit extends GuiToolkit {

    public GwtMaterialToolkit() {
        super(GwtPlatform.get().scheduler(), GwtWindow::new);
        registerNodeFactory(BorderPane.class, GwtBorderPane::new);
        registerNodeFactory(Table.class, GwtTable::new);
        registerNodeFactory(CheckBox.class, GwtMaterialCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, GwtMaterialCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtMaterialSearchBox::new);
    }
}
