package webfx.fxkits.extra.control;

import javafx.scene.control.Skin;
import webfx.fxkits.extra.displaydata.DisplayResult;

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
