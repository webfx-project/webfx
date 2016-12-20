package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.spi.viewer.base.GroupViewerBase;
import naga.toolkit.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxGroupViewer
        <FxN extends javafx.scene.Group, N extends Group, NV extends GroupViewerBase<N, NV, NM>, NM extends GroupViewerMixin<N, NV, NM>>

        extends FxNodeViewer<FxN, N, NV, NM>
        implements GroupViewerMixin<N, NV, NM> {

    public FxGroupViewer() {
        super((NV) new GroupViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.Group();
    }
}
