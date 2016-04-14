package naga.core.spi.gui.cn1;

import com.codename1.ui.Component;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import naga.core.spi.gui.GuiNode;
import naga.core.spi.gui.GuiToolkit;
import naga.core.spi.gui.cn1.nodes.Cn1BorderPane;
import naga.core.spi.gui.cn1.nodes.Cn1CheckBox;
import naga.core.spi.gui.cn1.nodes.Cn1SearchBox;
import naga.core.spi.gui.cn1.nodes.Cn1Table;
import naga.core.spi.gui.nodes.*;
import naga.core.spi.platform.Scheduler;


/**
 * @author Bruno Salmon
 */
public class CodenameOneToolkit extends GuiToolkit {

    public static void register() {
        register(new CodenameOneToolkit());
    }

    public CodenameOneToolkit() {
        registerNodeFactory(BorderPane.class, Cn1BorderPane::new);
        registerNodeFactory(Table.class, Cn1Table::new);
        registerNodeFactory(CheckBox.class, Cn1CheckBox::new);
        registerNodeFactory(ToggleSwitch.class, Cn1CheckBox::new);
        registerNodeFactory(SearchBox.class, Cn1SearchBox::new);
    }

    private GuiNode dontGarbageRootNode; // keeping reference to avoid garbage collection
    @Override
    public void displayRootNode(GuiNode rootNode) {
        dontGarbageRootNode = rootNode;
        Form form = new Form(new BorderLayout());
        form.add(BorderLayout.CENTER, (Component) rootNode.unwrapToToolkitNode());
        form.show();
    }

    @Override
    public Scheduler scheduler() {
        return Cn1Scheduler.SINGLETON;
    }
}
