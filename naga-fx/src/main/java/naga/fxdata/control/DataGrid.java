package naga.fxdata.control;

import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.SelectableDisplayResultSetControl;

/**
 * @author Bruno Salmon
 */
public class DataGrid extends SelectableDisplayResultSetControl {

    public DataGrid() {
    }

    public DataGrid(DisplayResultSet rs) {
        setDisplayResultSet(rs);
    }

}
