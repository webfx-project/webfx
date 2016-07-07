package naga.toolkit.providers.android;

import android.app.Activity;
import android.view.View;
import naga.toolkit.providers.android.nodes.controls.AndroidCheckBox;
import naga.toolkit.providers.android.nodes.controls.AndroidSearchBox;
import naga.toolkit.providers.android.nodes.controls.AndroidTable;
import naga.toolkit.providers.android.nodes.layouts.AndroidVPage;
import naga.toolkit.providers.android.nodes.layouts.AndroidWindow;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.CheckBox;
import naga.toolkit.spi.nodes.controls.SearchBox;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.spi.nodes.controls.ToggleSwitch;
import naga.toolkit.spi.nodes.layouts.VPage;
import naga.commons.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public class AndroidToolkit extends Toolkit {

    public static Activity currentActivity;

    public static void register() {
        Toolkit.register(new AndroidToolkit());
    }

    private AndroidToolkit() {
        super(AndroidGuiScheduler.SINGLETON, () -> new AndroidWindow(currentActivity));
        registerNodeFactory(VPage.class, AndroidVPage::new);
        registerNodeFactory(CheckBox.class, AndroidCheckBox::new);
        registerNodeFactory(ToggleSwitch.class, AndroidCheckBox::new);
        registerNodeFactory(SearchBox.class, AndroidSearchBox::new);
        registerNodeFactory(Table.class, new Factory<GuiNode>() { // For any reason AndroidTable::new produces a runtime error
            @Override
            public GuiNode create() {
                return new AndroidTable();
            }
        });
    }

    private int idSeq = 10000;

    @Override
    public <T extends GuiNode> T createNode(Class<T> nodeInterface) {
        T node = super.createNode(nodeInterface);
        if (node != null) {
            View view = (View) node.unwrapToNativeNode();
            view.setId(idSeq++);
        }
        return node;
    }
}
