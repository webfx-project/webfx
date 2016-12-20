package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.scene.Group;

/**
 * @author Bruno Salmon
 */
public class GroupViewerBase
        <N extends Group, NB extends GroupViewerBase<N, NB, NM>, NM extends GroupViewerMixin<N, NB, NM>>

        extends NodeViewerBase<N, NB, NM> {
}
