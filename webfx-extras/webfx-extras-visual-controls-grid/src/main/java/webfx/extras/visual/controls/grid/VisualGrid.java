package webfx.extras.visual.controls.grid;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import webfx.extras.visual.controls.grid.registry.VisualGridRegistry;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.controls.SelectableVisualResultControl;

/**
 * @author Bruno Salmon
 */
public class VisualGrid extends SelectableVisualResultControl {

    public VisualGrid() {
    }

    public VisualGrid(VisualResult rs) {
        setVisualResult(rs);
    }

    private final BooleanProperty headerVisibleProperty = new SimpleBooleanProperty(true);

    public BooleanProperty headerVisibleProperty() {
        return headerVisibleProperty;
    }

    public boolean isHeaderVisible() {
        return headerVisibleProperty.get();
    }

    public void setHeaderVisible(boolean headerVisible) {
        this.headerVisibleProperty.set(headerVisible);
    }

    private final BooleanProperty fullHeightProperty = new SimpleBooleanProperty(false);

    public BooleanProperty fullHeightProperty() {
        return fullHeightProperty;
    }

    public boolean isFullHeight() {
        return fullHeightProperty.get();
    }

    public void setFullHeight(boolean fullHeight) {
        this.fullHeightProperty.set(fullHeight);
    }

    static {
        VisualGridRegistry.registerDataGrid();
    }
}
