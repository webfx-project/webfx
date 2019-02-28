package webfx.fxkit.extra.controls.displaydata.datagrid;

import javafx.scene.control.Skin;
import webfx.fxkit.extra.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
public final class SkinnedDataGrid extends DataGrid {

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
