package naga.fxdata.spi.peer.base;

import emul.javafx.scene.Node;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayColumn;

/**
 * @author Bruno Salmon
 */
public interface DataGridPeerMixin
        <C, N extends DataGrid, NB extends DataGridPeerBase<C, N, NB, NM>, NM extends DataGridPeerMixin<C, N, NB, NM>>

        extends SelectableDisplayResultSetControlPeerMixin<C, N, NB, NM> {

    void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn);

    void setCellContent(C cell, Node content, DisplayColumn displayColumn);

}
