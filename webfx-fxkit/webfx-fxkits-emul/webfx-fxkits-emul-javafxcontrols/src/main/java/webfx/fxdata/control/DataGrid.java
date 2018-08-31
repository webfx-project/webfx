package webfx.fxdata.control;

import emul.javafx.beans.property.BooleanProperty;
import emul.javafx.beans.property.SimpleBooleanProperty;
import webfx.fxdata.displaydata.DisplayResult;

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
}
