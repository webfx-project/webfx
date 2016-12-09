package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.ext.control.DataGrid;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.spi.view.DataGridView;

/**
 * @author Bruno Salmon
 */
public interface DataGridViewMixin<C>
        extends DataGridView,
        SelectableDisplayResultSetControlViewMixin<C, DataGrid, DataGridViewBase<C>, DataGridViewMixin<C>> {

    void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn);

    void setCellContent(C cell, Node content, DisplayColumn displayColumn);

}
