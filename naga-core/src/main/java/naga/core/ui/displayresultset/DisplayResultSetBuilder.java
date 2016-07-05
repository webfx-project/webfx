package naga.core.ui.displayresultset;

/**
 * @author Bruno Salmon
 */
public class DisplayResultSetBuilder {

    private final int rowCount;
    private final Object[] values;
    private final DisplayColumn[] columns;

    private DisplayResultSetBuilder(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.values = new Object[rowCount * columnCount];
        this.columns = new DisplayColumn[columnCount];
    }

    public DisplayResultSetBuilder setDisplayColumn(int columnIndex, DisplayColumn displayColumn) {
        columns[columnIndex] = displayColumn;
        return this;
    }

    public DisplayResultSetBuilder setValue(int rowIndex, int columnIndex, Object value) {
        return setInlineValue(rowIndex + columnIndex * rowCount, value);
    }

    public DisplayResultSetBuilder setInlineValue(int inlineIndex, Object value) {
        values[inlineIndex] = value;
        return this;
    }

    public DisplayResultSet build() {
        return new DisplayResultSet(rowCount, values, columns);
    }

    public static DisplayResultSetBuilder create(int rowCount, int columnCount) {
        return new DisplayResultSetBuilder(rowCount, columnCount);
    }
}
