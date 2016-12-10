package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.GroupViewer;

/**
 * @author Bruno Salmon
 */
public interface GroupViewerMixin
        extends GroupViewer,
        NodeViewerMixin<Group, GroupViewerBase, GroupViewerMixin> {

}
