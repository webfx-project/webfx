package webfx.fxkits.extra.spi.peer.base;

import emul.javafx.scene.Node;
import webfx.fxkits.extra.control.DataGrid;
import webfx.fxkits.extra.displaydata.DisplayColumn;

/**
 * @author Bruno Salmon
 */
public interface DataGridPeerMixin
        <C, N extends DataGrid, NB extends DataGridPeerBase<C, N, NB, NM>, NM extends DataGridPeerMixin<C, N, NB, NM>>

        extends SelectableDisplayResultControlPeerMixin<C, N, NB, NM> {

    void updateHeaderVisible(boolean headerVisible);

    void updateFullHeight(boolean fullHeight);

    void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn);

    void setCellContent(C cell, Node content, DisplayColumn displayColumn);

}
