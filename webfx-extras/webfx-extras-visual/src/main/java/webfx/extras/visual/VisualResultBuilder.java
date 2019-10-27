package webfx.extras.visual;

import webfx.extras.visual.impl.VisualColumnImpl;
import webfx.extras.visual.impl.VisualResultImpl;
import webfx.platform.shared.util.function.Converter;

/**
 * @author Bruno Salmon
 */
public final class VisualResultBuilder {

    private final int rowCount;
    private final Object[] values;
    private final VisualColumn[] columns;

    private VisualResultBuilder(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.values = new Object[rowCount * columnCount];
        this.columns = new VisualColumnImpl[columnCount];
    }

    public VisualResultBuilder(int rowCount, VisualColumn... columns) {
        this.rowCount = rowCount;
        this.columns = columns;
        this.values = new Object[rowCount * columns.length];
    }

    public VisualResultBuilder setVisualColumn(int columnIndex, VisualColumn visualColumn) {
        columns[columnIndex] = visualColumn;
        return this;
    }

    public VisualResultBuilder setValue(int rowIndex, int columnIndex, Object value) {
        return setInlineValue(rowIndex + columnIndex * rowCount, value);
    }

    public VisualResultBuilder setInlineValue(int inlineIndex, Object value) {
        values[inlineIndex] = value;
        return this;
    }

    public VisualResult build() {
        return new VisualResultImpl(rowCount, values, columns);
    }

    public static VisualResultBuilder create(int rowCount, int columnCount) {
        return new VisualResultBuilder(rowCount, columnCount);
    }

    public static VisualResultBuilder create(int rowCount, VisualColumn[] columns) {
        return new VisualResultBuilder(rowCount, columns);
    }

    public static VisualResult convertVisualResult(VisualResult rs, Converter valueConverter) {
        int rowCount = rs.getRowCount();
        int columnCount = rs.getColumnCount();
        Object[] convertedValues = new Object[rowCount * columnCount];
        for (int columnIndex = 0, inlineIndex = 0; columnIndex < columnCount; columnIndex++)
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                convertedValues[inlineIndex++] = valueConverter.convert(rs.getValue(rowIndex, columnIndex));
        return new VisualResultImpl(rowCount, convertedValues, rs.getColumns());
    }
}
