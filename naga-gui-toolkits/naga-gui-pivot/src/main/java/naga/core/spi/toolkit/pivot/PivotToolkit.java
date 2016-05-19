package naga.core.spi.toolkit.pivot;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.nodes.*;
import naga.core.spi.toolkit.pivot.nodes.*;


/**
 * @author Bruno Salmon
 */
public class PivotToolkit extends Toolkit {

    public PivotToolkit() {
        super(PivotScheduler.SINGLETON, PivotWindow::new);
        registerNodeFactory(BorderPane.class, PivotBorderPane::new);
        registerNodeFactory(Table.class, PivotTable::new);
        registerNodeFactory(CheckBox.class, PivotCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, PivotCheckBox::new);
        registerNodeFactory(SearchBox.class, PivotSearchBox::new);
    }

}
