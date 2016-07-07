package naga.toolkit.providers.swing;

import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.*;
import naga.toolkit.spi.nodes.layouts.HBox;
import naga.toolkit.spi.nodes.layouts.VBox;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.toolkit.providers.swing.nodes.controls.SwingButton;
import naga.toolkit.providers.swing.nodes.controls.SwingCheckBox;
import naga.toolkit.providers.swing.nodes.controls.SwingTable;
import naga.toolkit.providers.swing.nodes.layouts.SwingHBox;
import naga.toolkit.providers.swing.nodes.layouts.SwingWindow;
import naga.toolkit.providers.swing.nodes.controls.SwingSearchBox;
import naga.toolkit.providers.swing.nodes.layouts.SwingVBox;
import naga.toolkit.providers.swing.nodes.layouts.SwingVPage;

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
