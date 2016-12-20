package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.Group;

/**
 * @author Bruno Salmon
 */
public interface GroupViewerMixin
        <N extends Group, NV extends GroupViewerBase<N, NV, NM>, NM extends GroupViewerMixin<N, NV, NM>>

        extends NodeViewerMixin<N, NV, NM> {
}
