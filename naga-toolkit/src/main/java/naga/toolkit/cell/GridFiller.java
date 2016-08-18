package naga.toolkit.cell;

import naga.commons.util.Strings;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.cell.renderers.CellRenderer;
import naga.toolkit.cell.renderers.CellRendererFactory;
import naga.toolkit.cell.renderers.ImageTextCellRenderer;
import naga.toolkit.cell.renderers.TextCellRenderer;

/**
 * @author Bruno Salmon
 */
public abstract class GridFiller<C> {

    private final GridAdapter<C, ?> gridAdapter;
    private final ImageTextGridAdapter<C, ?> imageTextGridAdapter;
    private int rowStyleColumnIndex;
    private DisplayResultSet rs;

    protected GridFiller(GridAdapter<C, ?> gridAdapter) {
        this.gridAdapter = gridAdapter;
        imageTextGridAdapter = gridAdapter instanceof ImageTextGridAdapter ? (ImageTextGridAdapter<C, ?>) gridAdapter : null;
    }

    public int getRowStyleColumnIndex() {
        return rowStyleColumnIndex;
    }

    public void fillGrid(DisplayResultSet rs) {
        this.rs = rs;
        rowStyleColumnIndex = -1;
        int columnCount = rs.getColumnCount();
        int gridColumnIndex = 0;
        DisplayColumn[] columns = rs.getColumns();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DisplayColumn displayColumn = columns[columnIndex];
            String role = displayColumn.getRole();
            if ("style".equals(role))
                rowStyleColumnIndex = columnIndex;
            else if (role == null) {
                CellRenderer cellRenderer = CellRendererFactory.getDefault().createCellRenderer(displayColumn.getType());
                setUpGridColumn(gridColumnIndex++, displayColumn, cellRenderer, columnIndex, rs);
            }
        }
    }

    protected abstract void setUpGridColumn(int gridColumnIndex, DisplayColumn displayColumn, CellRenderer cellRenderer, int rsColumnIndex, DisplayResultSet rs);

    public void fillCell(C cell, Object cellValue, CellRenderer cellRenderer, DisplayColumn displayColumn) {
        if (imageTextGridAdapter != null) {
            if (cellRenderer == TextCellRenderer.SINGLETON) {
                imageTextGridAdapter.setCellTextContent(cell, Strings.toString(cellValue), displayColumn);
                return;
            }
            if (cellRenderer == ImageTextCellRenderer.SINGLETON) {
                ImageTextCellRenderer imageTextCellRenderer = ImageTextCellRenderer.SINGLETON;
                Object[] array = imageTextCellRenderer.getAndCheckArray(cellValue);
                imageTextGridAdapter.setCellImageAndTextContent(cell, imageTextCellRenderer.getImage(array), imageTextCellRenderer.getText(array), displayColumn);
                return;
            }
        }
        gridAdapter.setCellContent(cell, cellRenderer.renderCellValue(cellValue), displayColumn);
    }

    public Object getRowStyleResultSetValue(int rowIndex) {
        int columnIndex = rowStyleColumnIndex;
        if (rowIndex < 0 || columnIndex < 0 || rowIndex >= rs.getRowCount() || columnIndex >= rs.getColumnCount())
            return null;
        return rs.getValue(rowIndex, columnIndex);
    }

    public Object[] getRowStyleClasses(int rowIndex) {
        Object value = getRowStyleResultSetValue(rowIndex);
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
