package naga.core.spi.gui.gwtmaterial;

import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.gwtmaterial.nodes.GwtBorderPane;
import naga.core.spi.gui.gwtmaterial.nodes.GwtMaterialCheckBox;
import naga.core.spi.gui.gwtmaterial.nodes.GwtMaterialSearchBox;
import naga.core.spi.gui.gwtmaterial.nodes.GwtTable;
import naga.core.spi.gui.nodes.*;

/**
 * @author Bruno Salmon
 */
public class GwtMaterialToolkit  extends GuiToolkit {

    public GwtMaterialToolkit() {
        registerNodeFactory(BorderPane.class, GwtBorderPane::new);
        registerNodeFactory(Table.class, GwtTable::new);
        registerNodeFactory(CheckBox.class, GwtMaterialCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, GwtMaterialCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtMaterialSearchBox::new);
    }

    @Override
    public void displayRootNode(GuiNode rootNode) {
        RootLayoutPanel.get().add((Widget) rootNode.unwrapToToolkitNode());
    }
}
