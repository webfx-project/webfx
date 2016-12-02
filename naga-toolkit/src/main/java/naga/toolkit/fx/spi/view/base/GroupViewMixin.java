package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.view.GroupView;

/**
 * @author Bruno Salmon
 */
public interface GroupViewMixin
        extends GroupView,
        NodeViewMixin<Group, GroupViewBase, GroupViewMixin> {

}
