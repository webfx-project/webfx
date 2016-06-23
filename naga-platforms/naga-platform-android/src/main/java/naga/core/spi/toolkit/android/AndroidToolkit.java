package naga.core.spi.toolkit.android;

import android.app.Activity;
import android.view.View;
import naga.core.spi.toolkit.android.controls.AndroidCheckBox;
import naga.core.spi.toolkit.android.controls.AndroidSearchBox;
import naga.core.spi.toolkit.android.controls.AndroidTable;
import naga.core.spi.toolkit.android.layouts.AndroidVPage;
import naga.core.spi.toolkit.android.layouts.AndroidWindow;
import naga.core.spi.toolkit.node.GuiNode;
import naga.core.spi.toolkit.Toolkit;
import naga.core.spi.toolkit.controls.CheckBox;
import naga.core.spi.toolkit.controls.SearchBox;
import naga.core.spi.toolkit.controls.Table;
import naga.core.spi.toolkit.controls.ToggleSwitch;
import naga.core.spi.toolkit.layouts.VPage;
import naga.core.util.function.Factory;

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
