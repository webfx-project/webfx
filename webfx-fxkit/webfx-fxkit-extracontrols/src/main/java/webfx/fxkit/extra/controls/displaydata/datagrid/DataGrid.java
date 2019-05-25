package webfx.fxkit.extra.controls.displaydata.datagrid;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import webfx.fxkit.extra.controls.displaydata.SelectableDisplayResultControl;
import webfx.fxkit.extra.controls.registry.ExtraControlsRegistry;
import webfx.fxkit.extra.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
public class DataGrid extends SelectableDisplayResultControl {

    public DataGrid() {
    }

    public DataGrid(DisplayResult rs) {
        setDisplayResult(rs);
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
        ExtraControlsRegistry.registerDataGrid();
    }
}
