package naga.core.spi.toolkit.gwt;

import com.google.gwt.user.cellview.client.CellTable;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.gwt.nodes.*;
import naga.core.spi.toolkit.nodes.*;
import naga.core.spi.platform.Platform;

/**
 * @author Bruno Salmon
 */
public class GwtToolkit extends Toolkit {

    public GwtToolkit() {
        super(Platform.get().scheduler(), GwtWindow::new);
        registerNodeFactory(VPage.class, GwtVPage::new);
        registerNodeFactory(VBox.class, GwtVBox::new);
        registerNodeFactoryAndWrapper(Table.class, GwtTable::new, CellTable.class, GwtTable::new);
        registerNodeFactory(CheckBox.class, GwtCheckBox::new);
        registerNodeFactory(Button.class, GwtButton::new);
        registerNodeFactory(ToggleSwitch.class, GwtCheckBox::new);
        registerNodeFactory(SearchBox.class, GwtSearchBox::new);
    }
}