package naga.toolkit.fxdata.control;

import naga.toolkit.fxdata.displaydata.DisplayResultSet;
import naga.toolkit.fxdata.SelectableDisplayResultSetControl;

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
