package naga.commons.services.query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class QueryResultSetBuilder {

    private int rowCount;
    private final int columnCount;
    private Object[] values;
    private String[] columnNames;

    private List<Object[]> growingRows;
    private Object[] currentRowValues;

    private QueryResultSetBuilder(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.values = new Object[rowCount * columnCount];
    }

    private QueryResultSetBuilder(int rowCount, String[] columnNames) {
        this(rowCount, columnNames.length);
        this.columnNames = columnNames;
    }

    private QueryResultSetBuilder(int columnCount) {
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

    public QueryResultSetBuilder setValue(int rowIndex, int columnIndex, Object value) {
        values[rowIndex + columnIndex * rowCount] = value;
        return this;
    }

    public void addRow() {
        growingRows.add(currentRowValues = new Object[columnCount]);
    }

    public void setCurrentRowValue(int columnIndex, Object value) {
        currentRowValues[columnIndex] = value;
    }

    public QueryResultSet build() {
        if (values == null) {
            values = new Object[rowCount * columnCount];
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++)
                    values[rowIndex + columnIndex * rowCount] = growingRows.get(rowIndex)[columnIndex];
        }
        return new QueryResultSet(rowCount, columnCount, values, columnNames);
    }

    public static QueryResultSetBuilder create(int rowCount, int columnCount) {
        return new QueryResultSetBuilder(rowCount, columnCount);
    }

    public static QueryResultSetBuilder create(int rowCount, String[] columnNames) {
        return new QueryResultSetBuilder(rowCount, columnNames);
    }

    public static QueryResultSetBuilder createUnknownRowCount(int columnCount) {
        return new QueryResultSetBuilder(columnCount);
    }
}
