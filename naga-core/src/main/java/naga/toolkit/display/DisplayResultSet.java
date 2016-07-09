package naga.toolkit.display;

import naga.commons.util.function.Converter;

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

    /**
     * @param valueConverter the value converter
     * @return a new DisplayResultSet with same properties but all values converted
     */
    DisplayResultSet convert(Converter valueConverter);
}
