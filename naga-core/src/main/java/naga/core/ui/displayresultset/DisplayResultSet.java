package naga.core.ui.displayresultset;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSet {

    /**
     * The number of rows in this display result.
     */
    private final int rowCount;

    /**
     * The number of columns in this display result.
     */
    private final int columnCount;

    /**
     * The values of the display result stored in an inline array.
     * First column, then 2nd column, etc... So inlineIndex = rowIndex + columnIndex * rowCount.
     */
    private final Object[] values;

    /**
     * The columns to display.
     */
    private final DisplayColumn[] columns;

    public DisplayResultSet(int rowCount, Object[] values, DisplayColumn[] columns) {
        this.rowCount = rowCount;
        this.values = values;
        this.columns = columns;
        columnCount = columns.length;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public Object[] getValues() {
        return values;
    }

    public DisplayColumn[] getColumns() {
        return columns;
    }

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
            sb.append(sb.length() == 1 ? "" : ", ").append(column.getHeaderValue()).append(" (").append(column.getType()).append(')');
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
