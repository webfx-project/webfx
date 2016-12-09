package naga.toolkit.fx.ext.control;

import naga.toolkit.fx.ext.SelectableDisplayResultSetControl;
import naga.toolkit.fx.ext.impl.DataGridImpl;

/**
 * @author Bruno Salmon
 */
public interface DataGrid extends SelectableDisplayResultSetControl {

    static DataGrid create() {
        return new DataGridImpl();
    }
}
