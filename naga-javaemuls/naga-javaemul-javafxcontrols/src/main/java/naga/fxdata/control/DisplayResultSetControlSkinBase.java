package naga.fxdata.control;

import emul.javafx.scene.Node;
import emul.javafx.scene.control.SkinBase;
import emul.javafx.scene.paint.*;
import naga.util.Strings;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fxdata.cell.renderer.ImageRenderer;
import naga.fxdata.cell.renderer.ImageTextRenderer;
import naga.fxdata.cell.renderer.TextRenderer;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.Label;

/**
 * @author Bruno Salmon
 */
public abstract class DisplayResultSetControlSkinBase<C extends DisplayResultSetControl, ROW extends Node, CELL extends Node> extends SkinBase<C> {

    private final boolean hasSpecialRenderingForImageAndText;

    public DisplayResultSetControlSkinBase(C control, boolean hasSpecialRenderingForImageAndText) {
        super(control);
        this.hasSpecialRenderingForImageAndText = hasSpecialRenderingForImageAndText;
    }

    private DisplayResultSet rs;
    private int gridColumnCount;
    private int rowStyleColumnIndex;
    private int rowBackgroundColumnIndex;

    protected void start() {
        Properties.runNowAndOnPropertiesChange(p -> updateResultSet(getSkinnable().getDisplayResultSet()), getSkinnable().displayResultSetProperty());
    }

    protected void updateResultSet(DisplayResultSet rs) {
        this.rs = rs;
        Toolkit.get().scheduler().runInUiThread(this::buildGrid);
    }

    protected void buildGrid() {
        startBuildingGrid();
        computeGridSizeAndSetUpColumns();
        buildRows();
        endBuildingGrid();
    }

    protected void buildRows() {
        if (rs != null) {
            int rowCount = rs.getRowCount();
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                buildRow(rowIndex);
        }
    }

    protected void buildRow(int rowIndex) {
        ROW bodyRow = getOrAddBodyRow(rowIndex);
        setUpBodyRow(bodyRow, rowIndex);
        buildRowCells(bodyRow, rowIndex);
    }

    protected void buildRowCells(ROW bodyRow, int rowIndex) {
        int columnCount = rs.getColumnCount();
        for (int columnIndex = 0, gridColumnIndex = 0; columnIndex < columnCount; columnIndex++) {
            if (isDataColumn(columnIndex))
                fillCell(getOrAddBodyRowCell(bodyRow, rowIndex, gridColumnIndex++), rowIndex, columnIndex);
        }
    }

    protected void startBuildingGrid() {};

    protected void endBuildingGrid() {};

    protected abstract CELL getOrAddHeadCell(int gridColumnIndex);

    protected abstract ROW getOrAddBodyRow(int rowIndex);

    protected void setUpBodyRow(ROW bodyRow, int rowIndex) {
        applyBodyRowStyleAndBackground(bodyRow, rowIndex);
    }

    protected void applyBodyRowStyleAndBackground(ROW bodyRow, int rowIndex) {
        applyBodyRowStyleAndBackground(bodyRow, rowIndex, getRowStyle(rowIndex), getRowBackground(rowIndex));
    }

    protected abstract void applyBodyRowStyleAndBackground(ROW bodyRow, int rowIndex, String rowStyle, Paint rowBackground);

    protected abstract CELL getOrAddBodyRowCell(ROW bodyRow, int rowIndex, int gridColumnIndex);

    protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
        Label label = displayColumn.getLabel();
        fillCell(getOrAddHeadCell(gridColumnIndex), new Object[]{label.getIconPath(), label.getText()}, displayColumn, ImageTextRenderer.SINGLETON);
    }

    protected void setCellContent(CELL cell, Node content, DisplayColumn displayColumn) {
    }

    protected void setCellTextContent(CELL cell, String text, DisplayColumn displayColumn) {
        setCellImageAndTextContent(cell, null, text, displayColumn);
    }

    protected void setCellImageContent(CELL cell, Node image, DisplayColumn displayColumn) {
        setCellContent(cell, image, displayColumn);
    }

    protected void setCellImageAndTextContent(CELL cell, Node image, String text, DisplayColumn displayColumn) {
    }

    public int getRowCount() {
        return rs == null ? 0 : rs.getRowCount();
    }

    public int getGridColumnCount() {
        return gridColumnCount;
    }

    public DisplayResultSet getRs() {
        return rs;
    }

    public void computeGridSizeWithoutSettingUpColumns() {
        computeGridSize(false);
    }

    public void computeGridSizeAndSetUpColumns() {
        computeGridSize(true);
    }

    private void computeGridSize(boolean setUpGridColumns) {
        rowStyleColumnIndex = rowBackgroundColumnIndex = -1;
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
                if (setUpGridColumns)
                    setUpGridColumn(gridColumnIndex++, columnIndex, displayColumn);
            } else if (role.equals("style"))
                rowStyleColumnIndex = columnIndex;
            else if (role.equals("background"))
                rowBackgroundColumnIndex = columnIndex;
        }
        gridColumnCount = gridColumnIndex;
    }

    public void fillCell(CELL cell, int rowIndex, int rsColumnIndex) {
        fillCell(cell, rowIndex, rsColumnIndex, rs.getColumns()[rsColumnIndex]);
    }

    public void fillCell(CELL cell, int rowIndex, int rsColumnIndex, DisplayColumn displayColumn) {
        fillCell(cell, rs.getValue(rowIndex, rsColumnIndex), displayColumn);
    }

    public void fillCell(CELL cell, Object cellValue, DisplayColumn displayColumn) {
        fillCell(cell, cellValue, displayColumn, displayColumn.getValueRenderer());
    }

    public void fillCell(CELL cell, Object cellValue, DisplayColumn displayColumn, ValueRenderer valueRenderer) {
        if (cell == null)
            return;
        if (hasSpecialRenderingForImageAndText) {
            if (valueRenderer == TextRenderer.SINGLETON) {
                setCellTextContent(cell, Strings.toString(cellValue), displayColumn);
                return;
            }
            if (valueRenderer == ImageRenderer.SINGLETON) {
                setCellImageContent(cell, valueRenderer.renderCellValue(cellValue), displayColumn);
                return;
            }
            if (valueRenderer == ImageTextRenderer.SINGLETON) {
                ImageTextRenderer imageTextCellRenderer = ImageTextRenderer.SINGLETON;
                Object[] array = imageTextCellRenderer.getAndCheckArray(cellValue);
                setCellImageAndTextContent(cell, imageTextCellRenderer.getImage(array), imageTextCellRenderer.getText(array), displayColumn);
                return;
            }
        }
        setCellContent(cell, valueRenderer.renderCellValue(cellValue), displayColumn);
    }


    public int getRowStyleColumnIndex() {
        return rowStyleColumnIndex;
    }

    public int getRowBackgroundColumnIndex() {
        return rowBackgroundColumnIndex;
    }

    public boolean isDataColumn(int columnIndex) {
        return columnIndex != rowStyleColumnIndex && columnIndex != rowBackgroundColumnIndex;
    }

    public int gridColumnIndexToResultSetColumnIndex(int gridColumnIndex, int rowStyleColumnIndex) {
        int rsColumnIndex = gridColumnIndex;
        if (rowStyleColumnIndex == 0 && gridColumnIndex >= rowStyleColumnIndex)
            rsColumnIndex++;
        return rsColumnIndex;
    }

    public Object getRowStyleResultSetValue(int rowIndex) {
        return getSafeResultSetValue(rowIndex, rowStyleColumnIndex);
    }

    public Object getRowBackgroundResultSetValue(int rowIndex) {
        return getSafeResultSetValue(rowIndex, rowBackgroundColumnIndex);
    }

    public Paint getRowBackground(Object value) {
        if (value instanceof String) {
            Paint paint = Paint.valueOf(value.toString());
            if (paint instanceof Color) {
                Color color = (Color) paint;
                paint = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, color.deriveColor(0, 1.0, 1.2, 1.0)), new Stop(1, color.deriveColor(0, 1.0, 0.8, 1.0)));
            }
            return paint;
        }
        return null;
    }

    public Paint getRowBackground(Integer rowIndex) {
        return getRowBackground(getRowBackgroundResultSetValue(rowIndex));
    }

    private Object getSafeResultSetValue(int rowIndex, int columnIndex) {
        if (rs == null || rowIndex < 0 || columnIndex < 0 || rowIndex >= rs.getRowCount() || columnIndex >= rs.getColumnCount())
            return null;
        return rs.getValue(rowIndex, columnIndex);
    }

    public Object[] getRowStyleClasses(Integer rowIndex) {
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