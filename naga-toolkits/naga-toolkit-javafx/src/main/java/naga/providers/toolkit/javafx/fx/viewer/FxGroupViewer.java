package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxGroupViewer
        extends FxNodeViewer<javafx.scene.Group, Group, GroupViewerBase, GroupViewerMixin>
        implements GroupViewerMixin {

    public FxGroupViewer() {
        super(new GroupViewerBase());
    }

    @Override
    protected javafx.scene.Group createFxNode() {
        return new javafx.scene.Group();
    }
}
