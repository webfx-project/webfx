package naga.fxdata.control;

import javafx.scene.Node;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
public abstract class SelectableDisplayResultSetControlSkinBase<C extends SelectableDisplayResultSetControl, ROW extends Node, CELL extends Node>
        extends DisplayResultSetControlSkinBase<C, ROW, CELL> {

    public SelectableDisplayResultSetControlSkinBase(C control, boolean hasSpecialRenderingForImageAndText) {
        super(control, hasSpecialRenderingForImageAndText);
    }

    @Override
    protected void start() {
        super.start();
        updateDisplaySelection(getSkinnable().getDisplaySelection(), null);
        getSkinnable().displaySelectionProperty().addListener((observable, oldValue, newValue) -> updateDisplaySelection(newValue, oldValue));
    }

    private void updateDisplaySelection(DisplaySelection selection, DisplaySelection oldSelection) {
        if (oldSelection != null)
            oldSelection.forEachRow(rowIndex -> getOrAddBodyRow(rowIndex).getStyleClass().remove("selected"));
        if (selection != null)
            selection.forEachRow(rowIndex -> getOrAddBodyRow(rowIndex).getStyleClass().add("selected"));
    }

    @Override
    protected void updateResultSet(DisplayResultSet rs) {
        getSkinnable().setDisplaySelection(null);
        super.updateResultSet(rs);
    }

    @Override
    protected void setUpBodyRow(ROW bodyRow, int rowIndex) {
        super.setUpBodyRow(bodyRow, rowIndex);
        bodyRow.setOnMouseClicked(e -> {
            C control = getSkinnable();
            DisplaySelection displaySelection = control.getDisplaySelection();
            if (displaySelection == null || displaySelection.getSelectedRow() != rowIndex)
                displaySelection = DisplaySelection.createSingleRowSelection(rowIndex);
            else
                displaySelection = null;
            control.setDisplaySelection(displaySelection);
        });
    }
}
