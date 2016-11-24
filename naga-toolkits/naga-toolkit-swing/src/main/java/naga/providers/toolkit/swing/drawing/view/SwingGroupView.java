package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.scene.Group;
import naga.toolkit.drawing.spi.view.base.GroupViewBase;
import naga.toolkit.drawing.spi.view.base.GroupViewMixin;

/**
 * @author Bruno Salmon
 */
public class SwingGroupView
        extends SwingNodeView<Group, GroupViewBase, GroupViewMixin>
        implements GroupViewMixin {

    public SwingGroupView() {
        super(new GroupViewBase());
    }
}
