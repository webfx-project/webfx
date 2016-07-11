package naga.providers.toolkit.swing;

import naga.providers.toolkit.swing.nodes.controls.SwingButton;
import naga.providers.toolkit.swing.nodes.controls.SwingCheckBox;
import naga.providers.toolkit.swing.nodes.controls.SwingSearchBox;
import naga.providers.toolkit.swing.nodes.controls.SwingTable;
import naga.providers.toolkit.swing.nodes.layouts.SwingHBox;
import naga.providers.toolkit.swing.nodes.layouts.SwingVBox;
import naga.providers.toolkit.swing.nodes.layouts.SwingVPage;
import naga.providers.toolkit.swing.nodes.layouts.SwingWindow;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;

import javax.swing.*;

/**
 * @author Bruno Salmon
 */
public class SwingToolkit extends Toolkit {

    static {
        try {
            UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored) {}
    }

    public SwingToolkit() {
        super(SwingScheduler.SINGLETON, SwingWindow::new);
        registerNodeFactory(VPage.class, SwingVPage::new);
        registerNodeFactory(Table.class, SwingTable::new);
        registerNodeFactory(CheckBox.class, SwingCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, SwingCheckBox::new);
        registerNodeFactory(SearchBox.class, SwingSearchBox::new);
        registerNodeFactoryAndWrapper(Button.class, SwingButton::new, JButton.class, SwingButton::new);
        registerNodeFactory(VBox.class, SwingVBox::new);
        registerNodeFactory(HBox.class, SwingHBox::new);
    }
}
