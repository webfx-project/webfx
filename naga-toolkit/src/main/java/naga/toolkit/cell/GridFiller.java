package naga.toolkit.cell;

import naga.commons.util.Strings;
import naga.toolkit.cell.renderers.ImageTextRenderer;
import naga.toolkit.cell.renderers.TextRenderer;
import naga.toolkit.cell.renderers.ValueRenderer;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public abstract class GridFiller<C> {

    private final GridAdapter<C, ?> gridAdapter;
    private final ImageTextGridAdapter<C, ?> imageTextGridAdapter;
    private DisplayResultSet rs;
    private int rowStyleColumnIndex;
    protected int gridColumnCount;

    protected GridFiller(GridAdapter<C, ?> gridAdapter) {
        this.gridAdapter = gridAdapter;
        imageTextGridAdapter = gridAdapter instanceof ImageTextGridAdapter ? (ImageTextGridAdapter<C, ?>) gridAdapter : null;
    }

    public void setDisplayResultSet(DisplayResultSet rs) {
        this.rs = rs;
    }

    protected DisplayResultSet getDisplayResultSet() {
        return rs;
    }

    public int getGridColumnCount() {
        return gridColumnCount;
    }

    public void initGrid(DisplayResultSet rs) {
        setDisplayResultSet(rs);
        fillGrid(true);
    }

    public void fillGrid(DisplayResultSet rs) {
        setDisplayResultSet(rs);
        fillGrid(false);
    }

    private void fillGrid(boolean init) {
        rowStyleColumnIndex = -1;
        int columnCount = rs.getColumnCount();
        int gridColumnIndex = 0;
        DisplayColumn[] columns = rs.getColumns();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            DisplayColumn displayColumn = columns[columnIndex];
            String role = displayColumn.getRole();
            if (role == null) {
                if (!init)
                    setUpGridColumn(gridColumnIndex, columnIndex, displayColumn);
                gridColumnIndex++;
            } else if (role.equals("style"))
                rowStyleColumnIndex = columnIndex;
        }
        gridColumnCount = gridColumnIndex;
    }

    protected abstract void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn);

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
        if (imageTextGridAdapter != null) {
            if (valueRenderer == TextRenderer.SINGLETON) {
                imageTextGridAdapter.setCellTextContent(cell, Strings.toString(cellValue), displayColumn);
                return;
            }
            if (valueRenderer == ImageTextRenderer.SINGLETON) {
                ImageTextRenderer imageTextCellRenderer = ImageTextRenderer.SINGLETON;
                Object[] array = imageTextCellRenderer.getAndCheckArray(cellValue);
                imageTextGridAdapter.setCellImageAndTextContent(cell, imageTextCellRenderer.getImage(array), imageTextCellRenderer.getText(array), displayColumn);
                return;
            }
        }
        gridAdapter.setCellContent(cell, valueRenderer.renderCellValue(cellValue), displayColumn);
    }

    public int getRowStyleColumnIndex() {
        return rowStyleColumnIndex;
    }

    public int gridColumnIndexToResultSetColumnIndex(int gridColumnIndex) {
        int rsColumnIndex = gridColumnIndex;
        if (rowStyleColumnIndex == 0 && gridColumnIndex >= rowStyleColumnIndex)
            rsColumnIndex++;
        return rsColumnIndex;
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
