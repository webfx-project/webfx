package naga.core.spi.toolkit.cn1;

import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.cn1.nodes.*;
import naga.core.spi.toolkit.nodes.*;


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
