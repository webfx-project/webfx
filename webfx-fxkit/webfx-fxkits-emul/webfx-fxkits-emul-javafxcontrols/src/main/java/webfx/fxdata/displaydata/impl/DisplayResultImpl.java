package webfx.fxdata.displaydata.impl;

import webfx.fxdata.displaydata.DisplayColumn;
import webfx.fxdata.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
public class DisplayResultImpl implements DisplayResult {

    private final int rowCount;
    private final int columnCount;
    private final DisplayColumn[] columns;
    /**
     * The values of the display result stored in an inline array.
     * First column, then 2nd column, etc... So inlineIndex = rowIndex + columnIndex * rowCount.
     */
    private final Object[] values;

    public DisplayResultImpl(int rowCount, Object[] values, DisplayColumn[] columns) {
        this.rowCount = rowCount;
        this.values = values;
        this.columns = columns;
        columnCount = columns.length;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public DisplayColumn[] getColumns() {
        return columns;
    }

    @Override
    public Object getValue(int rowIndex, int columnIndex) {
        return values[rowIndex + columnIndex * rowCount];
    }

    @Override
    public String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(StringBuilder sb) {
        sb.append('[');
        for (DisplayColumn column : columns)
            sb.append(sb.length() == 1 ? "" : ", ").append(column.getName()).append(" (").append(column.getType()).append(')');
        for (int rowIndex = 0; rowIndex < rowCount;) {
            sb.append("\n[");
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                sb.append(columnIndex == 0 ? "" : ", ").append(getValue(rowIndex, columnIndex));
            sb.append(']');
            if (++rowIndex < rowCount)
                sb.append(',');
        }
        sb.append("\n]");
        return sb;
    }

}
