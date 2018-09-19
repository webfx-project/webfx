package webfx.fxkits.extra.mapper.spi.peer.impl;

import webfx.fxkits.extra.displaydata.DisplayColumn;
import webfx.fxkits.extra.control.DataGrid;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface DataGridPeerImageTextMixin
        <C, N extends DataGrid, NB extends DataGridPeerBase<C, N, NB, NM>, NM extends DataGridPeerMixin<C, N, NB, NM>>

        extends DataGridPeerMixin<C, N, NB, NM> {

    default void setCellContent(C cell, Node content, DisplayColumn displayColumn) {
        setCellImageAndTextContent(cell, content, null, displayColumn);
    }

    default void setCellTextContent(C cell, String text, DisplayColumn displayColumn) {
        setCellImageAndTextContent(cell, null, text, displayColumn);
    }

    void setCellImageAndTextContent(C cell, Node image, String text, DisplayColumn displayColumn);

}
