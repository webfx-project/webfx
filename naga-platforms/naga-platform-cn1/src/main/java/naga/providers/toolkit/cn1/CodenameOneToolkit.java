package naga.providers.toolkit.cn1;

import naga.providers.toolkit.cn1.nodes.controls.Cn1CheckBox;
import naga.providers.toolkit.cn1.nodes.controls.Cn1SearchBox;
import naga.providers.toolkit.cn1.nodes.controls.Cn1Table;
import naga.providers.toolkit.cn1.nodes.layouts.Cn1VPage;
import naga.providers.toolkit.cn1.nodes.layouts.Cn1Window;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;
import naga.toolkit.spi.nodes.layouts.VPage;


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
