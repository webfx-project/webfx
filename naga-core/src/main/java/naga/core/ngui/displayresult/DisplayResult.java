package naga.core.ngui.displayresult;

import naga.core.type.Type;

/**
 * @author Bruno Salmon
 */
public class DisplayResult {

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
     * The types of the
     */
    private final Type[] columnTypes;

    /**
     * The labels to display for column headers
     */
    private final Object[] headerValues;

    private final Type headerType;

    public DisplayResult(int rowCount, Object[] values, Type[] columnTypes) {
        this(rowCount, values, columnTypes, null, null);
    }

    public DisplayResult(int rowCount, Object[] values, Type[] columnTypes, Object[] headerValues, Type headerType) {
        this.rowCount = rowCount;
        this.values = values;
        this.columnTypes = columnTypes;
        this.headerValues = headerValues;
        this.headerType = headerType;
        columnCount = columnTypes.length;
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

    public Type[] getColumnTypes() {
        return columnTypes;
    }

    public Object[] getHeaderValues() {
        return headerValues;
    }

    public Type getHeaderType() {
        return headerType;
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
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
            sb.append(columnIndex == 0 ? "" : ", ").append(headerValues[columnIndex]).append(" (").append(columnTypes[columnIndex]).append(')');
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
