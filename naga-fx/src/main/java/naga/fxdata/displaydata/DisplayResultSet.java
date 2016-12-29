package naga.fxdata.displaydata;

/**
 * @author Bruno Salmon
 */
public interface DisplayResultSet {

    /**
     * @return the number of rows in this display result
     */
    int getRowCount();

    /**
     * @return the number of columns in this display result
     */
    int getColumnCount();

    /**
     * @return The columns to display
     */
    DisplayColumn[] getColumns();

    /**
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the value specified at row and column indexes
     */
    Object getValue(int rowIndex, int columnIndex);
}
