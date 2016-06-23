package naga.core.spi.toolkit.gwt;

import com.google.gwt.user.cellview.client.CellTable;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.*;
import naga.core.spi.toolkit.gwt.controls.GwtButton;
import naga.core.spi.toolkit.gwt.controls.GwtCheckBox;
import naga.core.spi.toolkit.gwt.controls.GwtSearchBox;
import naga.core.spi.toolkit.gwt.controls.GwtTable;
import naga.core.spi.toolkit.gwt.layouts.GwtVBox;
import naga.core.spi.toolkit.gwt.layouts.GwtVPage;
import naga.core.spi.toolkit.gwt.layouts.GwtWindow;
import naga.core.spi.toolkit.layouts.VBox;
import naga.core.spi.toolkit.layouts.VPage;
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