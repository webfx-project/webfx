package naga.fxdata.control;

import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public class DataGrid extends SelectableDisplayResultSetControl {

    public DataGrid() {
        getStyleClass().add("grid");
    }

    public DataGrid(DisplayResultSet rs) {
        setDisplayResultSet(rs);
    }

}
