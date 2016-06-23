package naga.core.spi.toolkit.cn1;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.cn1.controls.Cn1CheckBox;
import naga.core.spi.toolkit.cn1.controls.Cn1SearchBox;
import naga.core.spi.toolkit.cn1.controls.Cn1Table;
import naga.core.spi.toolkit.cn1.layouts.Cn1VPage;
import naga.core.spi.toolkit.cn1.layouts.Cn1Window;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.controls.ToggleSwitch;
import naga.core.spi.toolkit.layouts.VPage;


/**
 * @author Bruno Salmon
 */
public class CodenameOneToolkit extends Toolkit {

    public static void register() {
        register(new CodenameOneToolkit());
    }

    public CodenameOneToolkit() {
        super(Cn1Scheduler.SINGLETON, Cn1Window::new);
        registerNodeFactory(VPage.class, Cn1VPage::new);
        registerNodeFactory(Table.class, Cn1Table::new);
        registerNodeFactory(CheckBox.class, Cn1CheckBox::new);
        registerNodeFactory(ToggleSwitch.class, Cn1CheckBox::new);
        registerNodeFactory(SearchBox.class, Cn1SearchBox::new);
    }
}
