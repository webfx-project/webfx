package naga.toolkit.fx.spi.viewer.base;

import naga.commons.util.Strings;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.cell.renderer.ImageTextRenderer;
import naga.toolkit.fx.ext.cell.renderer.TextRenderer;
import naga.toolkit.fx.ext.cell.renderer.ValueRenderer;
import naga.toolkit.fx.ext.control.DataGrid;

/**
 * @author Bruno Salmon
 */
public class DataGridViewerBase
        <C, N extends DataGrid, NV extends DataGridViewerBase<C, N, NV, NM>, NM extends DataGridViewerMixin<C, N, NV, NM>>

        extends SelectableDisplayResultSetControlViewerBase<C, N, NV, NM> {

    private int rowStyleColumnIndex;
    private int gridColumnCount;
    private DisplayResultSet rs;
    private DataGridViewerImageTextMixin<C, N, NV, NM> imageTextMixin;

    public int getGridColumnCount() {
        return gridColumnCount;
    }

    public DisplayResultSet getRs() {
        return rs;
    }

    @Override
    public void setMixin(NM mixin) {
        super.setMixin(mixin);
        imageTextMixin = mixin instanceof DataGridViewerImageTextMixin ? (DataGridViewerImageTextMixin<C, N, NV, NM>) mixin : null;
    }

    public void initGrid(DisplayResultSet rs) {
        this.rs = rs;
        fillGrid(true);
    }

    public void fillGrid(DisplayResultSet rs) {
        this.rs = rs;
        fillGrid(false);
    }


    public void fillGrid(boolean init) {
        rowStyleColumnIndex = -1;
        gridColumnCount = 0;
        if (rs == null)
            return;
        int columnCount = rs.getColumnCount();
        int gridColumnIndex = 0;
        DisplayColumn[] columns = rs.getColumns();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DisplayColumn displayColumn = columns[columnIndex];
            String role = displayColumn.getRole();
            if (role == null) {
                if (!init)
                    mixin.setUpGridColumn(gridColumnIndex, columnIndex, displayColumn);
                gridColumnIndex++;
            } else if (role.equals("style"))
                rowStyleColumnIndex = columnIndex;
        }
        gridColumnCount = gridColumnIndex;
    }

    public void fillCell(C cell, int rowIndex, int rsColumnIndex) {
        fillCell(cell, rowIndex, rsColumnIndex, rs.getColumns()[rsColumnIndex]);
    }

    public void fillCell(C cell, int rowIndex, int rsColumnIndex, DisplayColumn displayColumn) {
        fillCell(cell, rs.getValue(rowIndex, rsColumnIndex), displayColumn);
    }

    public void fillCell(C cell, Object cellValue, DisplayColumn displayColumn) {
        fillCell(cell, cellValue, displayColumn, displayColumn.getValueRenderer());
    }

    public void fillCell(C cell, Object cellValue, DisplayColumn displayColumn, ValueRenderer valueRenderer) {
        if (imageTextMixin != null) {
            if (valueRenderer == TextRenderer.SINGLETON) {
                imageTextMixin.setCellTextContent(cell, Strings.toString(cellValue), displayColumn);
                return;
            }
            if (valueRenderer == ImageTextRenderer.SINGLETON) {
                ImageTextRenderer imageTextCellRenderer = ImageTextRenderer.SINGLETON;
                Object[] array = imageTextCellRenderer.getAndCheckArray(cellValue);
                imageTextMixin.setCellImageAndTextContent(cell, imageTextCellRenderer.getImage(array), imageTextCellRenderer.getText(array), displayColumn);
                return;
            }
        }
        mixin.setCellContent(cell, valueRenderer.renderCellValue(cellValue), displayColumn);
    }

    public int getRowStyleColumnIndex() {
        return rowStyleColumnIndex;
    }

    public int gridColumnIndexToResultSetColumnIndex(int gridColumnIndex, int rowStyleColumnIndex) {
        int rsColumnIndex = gridColumnIndex;
        if (rowStyleColumnIndex == 0 && gridColumnIndex >= rowStyleColumnIndex)
            rsColumnIndex++;
        return rsColumnIndex;
    }

    public Object getRowStyleResultSetValue(int rowIndex) {
        int columnIndex = rowStyleColumnIndex;
        if (rs == null || rowIndex < 0 || columnIndex < 0 || rowIndex >= rs.getRowCount() || columnIndex >= rs.getColumnCount())
            return null;
        return rs.getValue(rowIndex, columnIndex);
    }

    public Object[] getRowStyleClasses(int rowIndex) {
        return getRowStyleClasses(getRowStyleResultSetValue(rowIndex));
    }

    public Object[] getRowStyleClasses(Object value) {
        if (!(value instanceof Object[]))
            return null;
        return (Object[]) value;
    }

    public String getRowStyle(int rowIndex) {
        Object[] styleClasses = getRowStyleClasses(rowIndex);
        if (styleClasses == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (Object styleClass : styleClasses) {
            if (styleClass != null) {
                if (sb.length() > 0)
                    sb.append(' ');
                sb.append(styleClass);
            }
        }
        return sb.toString().trim();
    }
}
