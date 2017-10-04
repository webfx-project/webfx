package naga.fxdata.control;

import javafx.scene.control.Skin;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public class SkinnedDataGrid extends DataGrid {

    public SkinnedDataGrid() {
    }

    public SkinnedDataGrid(DisplayResultSet rs) {
        super(rs);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DataGridSkin(this);
    }
}
