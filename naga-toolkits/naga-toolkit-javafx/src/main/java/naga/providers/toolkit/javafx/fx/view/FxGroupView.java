package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.view.base.GroupViewBase;
import naga.toolkit.fx.spi.view.base.GroupViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxGroupView
        extends FxNodeView<javafx.scene.Group, Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public FxGroupView() {
        super(new GroupViewBase());
    }

    @Override
    javafx.scene.Group createFxNode() {
        return new javafx.scene.Group();
    }
}
