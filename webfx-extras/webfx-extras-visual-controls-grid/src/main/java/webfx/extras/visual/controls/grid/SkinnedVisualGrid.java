package webfx.extras.visual.controls.grid;

import javafx.scene.control.Skin;
import webfx.extras.visual.VisualResult;

/**
 * @author Bruno Salmon
 */
public final class SkinnedVisualGrid extends VisualGrid {

    public SkinnedVisualGrid() {
    }

    public SkinnedVisualGrid(VisualResult rs) {
        super(rs);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new VisualGridSkin(this);
    }
}
