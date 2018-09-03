package webfx.fxkits.extra.displaydata;

import webfx.fxkits.extra.displaydata.impl.DisplayResultImpl;
import webfx.platforms.core.util.function.Converter;
import webfx.fxkits.extra.displaydata.impl.DisplayColumnImpl;

/**
 * @author Bruno Salmon
 */
public class DisplayResultBuilder {

    private final int rowCount;
    private final Object[] values;
    private final DisplayColumn[] columns;

    private DisplayResultBuilder(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.values = new Object[rowCount * columnCount];
        this.columns = new DisplayColumnImpl[columnCount];
    }

    private DisplayResultBuilder(int rowCount, DisplayColumn[] columns) {
        this.rowCount = rowCount;
        this.columns = columns;
        this.values = new Object[rowCount * columns.length];
    }

    public DisplayResultBuilder setDisplayColumn(int columnIndex, DisplayColumn displayColumn) {
        columns[columnIndex] = displayColumn;
        return this;
    }

    public DisplayResultBuilder setValue(int rowIndex, int columnIndex, Object value) {
        return setInlineValue(rowIndex + columnIndex * rowCount, value);
    }

    public DisplayResultBuilder setInlineValue(int inlineIndex, Object value) {
        values[inlineIndex] = value;
        return this;
    }

    public DisplayResult build() {
        return new DisplayResultImpl(rowCount, values, columns);
    }

    public static DisplayResultBuilder create(int rowCount, int columnCount) {
        return new DisplayResultBuilder(rowCount, columnCount);
    }

    public static DisplayResultBuilder create(int rowCount, DisplayColumn[] columns) {
        return new DisplayResultBuilder(rowCount, columns);
    }

    public static DisplayResult convertDisplayResult(DisplayResult rs, Converter valueConverter) {
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        Object[] convertedValues = new Object[rowCount * columnCount];
        for (int columnIndex = 0, inlineIndex = 0; columnIndex < columnCount; columnIndex++)
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                convertedValues[inlineIndex++] = valueConverter.convert(rs.getValue(rowIndex, columnIndex));
        return new DisplayResultImpl(rowCount, convertedValues, rs.getColumns());
    }
}
