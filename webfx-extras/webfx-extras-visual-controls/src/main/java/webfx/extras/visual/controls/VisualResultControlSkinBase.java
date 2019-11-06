package webfx.extras.visual.controls;

import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.*;
import webfx.extras.cell.renderer.ImageRenderer;
import webfx.extras.cell.renderer.ImageTextRenderer;
import webfx.extras.cell.renderer.TextRenderer;
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.visual.VisualColumn;
import webfx.extras.visual.VisualResult;
import webfx.extras.label.Label;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;

/**
 * @author Bruno Salmon
 */
public abstract class VisualResultControlSkinBase<C extends VisualResultControl, ROW extends Node, CELL extends Node> extends SkinBase<C> {

    private final boolean hasSpecialRenderingForImageAndText;

    public VisualResultControlSkinBase(C control, boolean hasSpecialRenderingForImageAndText) {
        super(control);
        this.hasSpecialRenderingForImageAndText = hasSpecialRenderingForImageAndText;
    }

    private VisualResult rs;
    private int gridColumnCount;
    private int rowStyleColumnIndex;
    private int rowBackgroundColumnIndex;

    protected void start() {
        Properties.runNowAndOnPropertiesChange(() -> updateResult(getSkinnable().getVisualResult()), getSkinnable().visualResultProperty());
    }

    protected void updateResult(VisualResult rs) {
        this.rs = rs;
        UiScheduler.runInUiThread(this::buildGrid);
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

    protected void startBuildingGrid() {}

    protected void endBuildingGrid() {}

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

    protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, VisualColumn visualColumn) {
        Label label = visualColumn.getLabel();
        fillCell(getOrAddHeadCell(gridColumnIndex), new Object[]{label.getIconPath(), label.getText()}, visualColumn, ImageTextRenderer.SINGLETON);
    }

    protected void setCellContent(CELL cell, Node content, VisualColumn visualColumn) {
    }

    protected void setCellTextContent(CELL cell, String text, VisualColumn visualColumn) {
        setCellImageAndTextContent(cell, null, text, visualColumn);
    }

    protected void setCellImageContent(CELL cell, Node image, VisualColumn visualColumn) {
        setCellContent(cell, image, visualColumn);
    }

    protected void setCellImageAndTextContent(CELL cell, Node image, String text, VisualColumn visualColumn) {
    }

    public int getRowCount() {
        return rs == null ? 0 : rs.getRowCount();
    }

    public int getGridColumnCount() {
        return gridColumnCount;
    }

    public VisualResult getRs() {
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
        VisualColumn[] columns = rs.getColumns();
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            VisualColumn visualColumn = columns[columnIndex];
            String role = visualColumn.getRole();
            if (role == null) {
                if (setUpGridColumns)
                    setUpGridColumn(gridColumnIndex++, columnIndex, visualColumn);
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

    public void fillCell(CELL cell, int rowIndex, int rsColumnIndex, VisualColumn visualColumn) {
        fillCell(cell, rs.getValue(rowIndex, rsColumnIndex), visualColumn);
    }

    public void fillCell(CELL cell, Object cellValue, VisualColumn visualColumn) {
        fillCell(cell, cellValue, visualColumn, visualColumn.getValueRenderer());
    }

    public void fillCell(CELL cell, Object cellValue, VisualColumn visualColumn, ValueRenderer valueRenderer) {
        if (cell == null)
            return;
        if (hasSpecialRenderingForImageAndText) {
            if (valueRenderer == TextRenderer.SINGLETON) {
                //setCellTextContent(cell, Strings.toString(cellValue), displayColumn);
                return;
            }
            if (valueRenderer == ImageRenderer.SINGLETON) {
                //setCellImageContent(cell, valueRenderer.renderCellValue(cellValue, ValueRenderingContext.DEFAULT_READONLY_CONTEXT), displayColumn);
                return;
            }
            if (valueRenderer == ImageTextRenderer.SINGLETON) {
                ImageTextRenderer imageTextCellRenderer = ImageTextRenderer.SINGLETON;
                Object[] array = imageTextCellRenderer.getAndCheckArray(cellValue);
                setCellImageAndTextContent(cell, imageTextCellRenderer.getImage(array), imageTextCellRenderer.getText(array), visualColumn);
                return;
            }
        }
        setCellContent(cell, valueRenderer.renderValue(cellValue, visualColumn.getValueRenderingContext()), visualColumn);
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

    public int gridColumnIndexToResultColumnIndex(int gridColumnIndex, int rowStyleColumnIndex) {
        int rsColumnIndex = gridColumnIndex;
        if (rowStyleColumnIndex == 0 && gridColumnIndex >= rowStyleColumnIndex)
            rsColumnIndex++;
        return rsColumnIndex;
    }

    public Object getRowStyleResultValue(int rowIndex) {
        return getSafeResultValue(rowIndex, rowStyleColumnIndex);
    }

    public Object getRowBackgroundResultValue(int rowIndex) {
        return getSafeResultValue(rowIndex, rowBackgroundColumnIndex);
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
        return getRowBackground(getRowBackgroundResultValue(rowIndex));
    }

    private Object getSafeResultValue(int rowIndex, int columnIndex) {
        if (rs == null || rowIndex < 0 || columnIndex < 0 || rowIndex >= rs.getRowCount() || columnIndex >= rs.getColumnCount())
            return null;
        return rs.getValue(rowIndex, columnIndex);
    }

    public Object[] getRowStyleClasses(Integer rowIndex) {
        return getRowStyleClasses(getRowStyleResultValue(rowIndex));
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
