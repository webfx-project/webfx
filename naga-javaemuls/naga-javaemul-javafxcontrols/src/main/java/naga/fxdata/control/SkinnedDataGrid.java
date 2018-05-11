package naga.fxdata.control;

import emul.javafx.scene.control.Skin;
import naga.fxdata.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
public class SkinnedDataGrid extends DataGrid {

    public SkinnedDataGrid() {
    }

    public SkinnedDataGrid(DisplayResult rs) {
        super(rs);
    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new DataGridSkin(this);
    }
}
