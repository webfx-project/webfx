package naga.toolkit.spi.display;

import naga.toolkit.spi.display.impl.DisplayColumnImpl;
import naga.toolkit.spi.display.impl.DisplayResultSetImpl;

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
        this.columns = new DisplayColumnImpl[columnCount];
    }

    private DisplayResultSetBuilder(int rowCount, DisplayColumn[] columns) {
        this.rowCount = rowCount;
        this.columns = columns;
        this.values = new Object[rowCount * columns.length];
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
        return new DisplayResultSetImpl(rowCount, values, columns);
    }

    public static DisplayResultSetBuilder create(int rowCount, int columnCount) {
        return new DisplayResultSetBuilder(rowCount, columnCount);
    }

    public static DisplayResultSetBuilder create(int rowCount, DisplayColumn[] columns) {
        return new DisplayResultSetBuilder(rowCount, columns);
    }
}
