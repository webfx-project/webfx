package naga.fx.spi.javafx.fx.viewer;

import naga.fx.scene.Group;
import naga.fx.spi.viewer.base.GroupViewerBase;
import naga.fx.spi.viewer.base.GroupViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxGroupViewer
        <FxN extends javafx.scene.Group, N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends FxNodeViewer<FxN, N, NB, NM>
        implements GroupViewerMixin<N, NB, NM> {

    public FxGroupViewer() {
        super((NB) new GroupViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.Group();
    }
}
