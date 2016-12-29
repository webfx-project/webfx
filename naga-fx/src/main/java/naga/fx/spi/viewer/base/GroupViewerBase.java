package naga.fx.spi.viewer.base;

import naga.fx.scene.Group;

/**
 * @author Bruno Salmon
 */
public class GroupViewerBase
        <N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends NodeViewerBase<N, NB, NM> {
}
