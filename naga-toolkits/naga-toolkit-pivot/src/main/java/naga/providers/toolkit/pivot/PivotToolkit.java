package naga.providers.toolkit.pivot;

import naga.providers.toolkit.pivot.nodes.controls.*;
import naga.providers.toolkit.pivot.nodes.layouts.PivotHBox;
import naga.providers.toolkit.pivot.nodes.layouts.PivotVBox;
import naga.providers.toolkit.pivot.nodes.layouts.PivotVPage;
import naga.providers.toolkit.pivot.nodes.layouts.PivotWindow;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import org.apache.pivot.wtk.PushButton;


/**
 * @author Bruno Salmon
 */
public class PivotToolkit extends Toolkit {

    public PivotToolkit() {
        super(PivotScheduler.SINGLETON, PivotWindow::new);
        registerNodeFactory(VBox.class, PivotVBox::new);
        registerNodeFactory(HBox.class, PivotHBox::new);
        registerNodeFactory(VPage.class, PivotVPage::new);
        registerNodeFactory(Table.class, PivotTable::new);
        registerNodeFactoryAndWrapper(CheckBox.class, PivotCheckBox::new, org.apache.pivot.wtk.Checkbox.class, PivotCheckBox::new);
        registerNodeFactoryAndWrapper(naga.toolkit.spi.nodes.controls.Button.class, PivotButton::new, PushButton.class, PivotButton::new);
        registerNodeFactoryAndWrapper(naga.toolkit.spi.nodes.controls.Slider.class, PivotSlider::new, org.apache.pivot.wtk.Slider.class, PivotSlider::new);
        registerNodeFactory(ToggleSwitch.class, PivotCheckBox::new);
        registerNodeFactory(SearchBox.class, PivotSearchBox::new);
    }

}
