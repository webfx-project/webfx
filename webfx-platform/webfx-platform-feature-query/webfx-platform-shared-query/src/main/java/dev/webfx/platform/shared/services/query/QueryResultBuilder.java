package dev.webfx.platform.shared.services.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class QueryResultBuilder {

    private int rowCount;
    private final int columnCount;
    private Object[] values;
    private String[] columnNames;

    private List<Object[]> growingRows;
    private Object[] currentRowValues;

    private QueryResultBuilder(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.values = new Object[rowCount * columnCount];
    }

    private QueryResultBuilder(int rowCount, String[] columnNames) {
        this(rowCount, columnNames.length);
        this.columnNames = columnNames;
    }

    private QueryResultBuilder(int columnCount) {
        this.columnCount = columnCount;
        this.growingRows = new ArrayList<>(100); // Default capacity = 100 (as default limit is often 100).
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public void setColumnName(int columnIndex, String columnName) {
        if (columnNames == null)
            columnNames = new String[columnCount];
        columnNames[columnIndex] = columnName;
    }

    public QueryResultBuilder setValue(int rowIndex, int columnIndex, Object value) {
        values[rowIndex + columnIndex * rowCount] = value;
        return this;
    }

    public void addRow() {
        growingRows.add(currentRowValues = new Object[columnCount]);
    }

    public void setCurrentRowValue(int columnIndex, Object value) {
        currentRowValues[columnIndex] = value;
    }

    public QueryResult build() {
        if (values == null) {
            rowCount = growingRows.size();
            values = new Object[rowCount * columnCount];
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                    values[rowIndex + columnIndex * rowCount] = growingRows.get(rowIndex)[columnIndex];
        }
        return new QueryResult(rowCount, columnCount, values, columnNames);
    }

    public static QueryResultBuilder create(int rowCount, int columnCount) {
        return new QueryResultBuilder(rowCount, columnCount);
    }

    public static QueryResultBuilder create(int rowCount, String[] columnNames) {
        return new QueryResultBuilder(rowCount, columnNames);
    }

    public static QueryResultBuilder createUnknownRowCount(int columnCount) {
        return new QueryResultBuilder(columnCount);
    }
}
