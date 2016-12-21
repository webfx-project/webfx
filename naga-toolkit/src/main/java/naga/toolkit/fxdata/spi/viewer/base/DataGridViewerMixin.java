package naga.toolkit.fxdata.spi.viewer.base;

import naga.toolkit.fxdata.displaydata.DisplayColumn;
import naga.toolkit.fxdata.control.DataGrid;
import naga.toolkit.fx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface DataGridViewerMixin
        <C, N extends DataGrid, NB extends DataGridViewerBase<C, N, NB, NM>, NM extends DataGridViewerMixin<C, N, NB, NM>>

        extends SelectableDisplayResultSetControlViewerMixin<C, N, NB, NM> {

    void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn);

    void setCellContent(C cell, Node content, DisplayColumn displayColumn);

}
