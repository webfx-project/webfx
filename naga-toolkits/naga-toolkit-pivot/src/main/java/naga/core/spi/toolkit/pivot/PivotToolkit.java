package naga.core.spi.toolkit.pivot;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.*;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.pivot.controls.PivotButton;
import naga.core.spi.toolkit.pivot.controls.PivotCheckBox;
import naga.core.spi.toolkit.pivot.controls.PivotSearchBox;
import naga.core.spi.toolkit.pivot.controls.PivotTable;
import naga.core.spi.toolkit.pivot.layouts.PivotVPage;
import naga.core.spi.toolkit.pivot.layouts.PivotWindow;
import org.apache.pivot.wtk.PushButton;


/**
 * @author Bruno Salmon
 */
public class PivotToolkit extends Toolkit {

    public PivotToolkit() {
        super(PivotScheduler.SINGLETON, PivotWindow::new);
        registerNodeFactory(VPage.class, PivotVPage::new);
        registerNodeFactory(Table.class, PivotTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, PivotCheckBox::new, org.apache.pivot.wtk.Checkbox.class, PivotCheckBox::new);
        registerNodeFactoryAndWrapper(Button.class, PivotButton::new, PushButton.class, PivotButton::new);
        registerNodeFactory(ToggleSwitch.class, PivotCheckBox::new);
        registerNodeFactory(SearchBox.class, PivotSearchBox::new);
    }

}
