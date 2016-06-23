package naga.core.spi.toolkit.pivot;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.controls.ToggleSwitch;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.pivot.controls.PivotCheckBox;
import naga.core.spi.toolkit.pivot.controls.PivotSearchBox;
import naga.core.spi.toolkit.pivot.controls.PivotTable;
import naga.core.spi.toolkit.pivot.layouts.PivotVPage;
import naga.core.spi.toolkit.pivot.layouts.PivotWindow;


/**
 * @author Bruno Salmon
 */
public class PivotToolkit extends Toolkit {

    public PivotToolkit() {
        super(PivotScheduler.SINGLETON, PivotWindow::new);
        registerNodeFactory(VPage.class, PivotVPage::new);
        registerNodeFactory(Table.class, PivotTable::new);
        registerNodeFactory(CheckBox.class, PivotCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, PivotCheckBox::new);
        registerNodeFactory(SearchBox.class, PivotSearchBox::new);
    }

}
