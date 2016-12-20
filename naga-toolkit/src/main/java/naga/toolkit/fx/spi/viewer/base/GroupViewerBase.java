package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.fx.scene.Group;

/**
 * @author Bruno Salmon
 */
public class GroupViewerBase
        <N extends Group, NV extends GroupViewerBase<N, NV, NM>, NM extends GroupViewerMixin<N, NV, NM>>

        extends NodeViewerBase<N, NV, NM> {
}
