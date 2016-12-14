package naga.toolkit.fx.ext.impl;

import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.fx.ext.control.DataGrid;

/**
 * @author Bruno Salmon
 */
public class DataGridImpl extends SelectableDisplayResultSetControlImpl implements DataGrid {

    public DataGridImpl() {
    }

    public DataGridImpl(DisplayResultSet rs) {
        setDisplayResultSet(rs);
    }

}
