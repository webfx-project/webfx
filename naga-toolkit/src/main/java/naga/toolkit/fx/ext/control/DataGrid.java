package naga.toolkit.fx.ext.control;

import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;

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
