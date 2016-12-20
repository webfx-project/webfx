package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.Group;

/**
 * @author Bruno Salmon
 */
public interface GroupViewerMixin
        <N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends NodeViewerMixin<N, NB, NM> {
}
