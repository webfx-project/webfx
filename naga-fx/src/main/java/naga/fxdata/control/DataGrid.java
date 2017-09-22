package naga.fxdata.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Skin;
import naga.fxdata.displaydata.DisplayResultSet;

/**
 * @author Bruno Salmon
 */
public class DataGrid extends SelectableDisplayResultSetControl {

    public DataGrid() {
        getStyleClass().add("grid");
    }

    public DataGrid(DisplayResultSet rs) {
        setDisplayResultSet(rs);
    }

    private final BooleanProperty headerVisibleProperty = new SimpleBooleanProperty(true);

    public BooleanProperty headerVisibleProperty() {
        return headerVisibleProperty;
    }

    public boolean isHeaderVisible() {
        return headerVisibleProperty.get();
    }

    public void setHeaderVisible(boolean showHeaderProperty) {
        this.headerVisibleProperty.set(showHeaderProperty);
    }
}
