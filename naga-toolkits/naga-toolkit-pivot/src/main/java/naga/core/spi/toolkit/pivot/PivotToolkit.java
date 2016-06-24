package naga.core.spi.toolkit.pivot;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.*;
import naga.core.spi.toolkit.controls.Button;
import naga.core.spi.toolkit.controls.Slider;
import naga.core.spi.toolkit.layouts.HBox;
import naga.core.spi.toolkit.layouts.VBox;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.spi.toolkit.pivot.controls.*;
import naga.core.spi.toolkit.pivot.layouts.PivotHBox;
import naga.core.spi.toolkit.pivot.layouts.PivotVBox;
import naga.core.spi.toolkit.pivot.layouts.PivotVPage;
import naga.core.spi.toolkit.pivot.layouts.PivotWindow;
import org.apache.pivot.wtk.*;


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
        registerNodeFactoryAndWrapper(Button.class, PivotButton::new, PushButton.class, PivotButton::new);
        registerNodeFactoryAndWrapper(Slider.class, PivotSlider::new, org.apache.pivot.wtk.Slider.class, PivotSlider::new);
        registerNodeFactory(ToggleSwitch.class, PivotCheckBox::new);
        registerNodeFactory(SearchBox.class, PivotSearchBox::new);
    }

}
