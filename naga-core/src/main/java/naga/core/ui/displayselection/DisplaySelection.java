package naga.core.ui.displayselection;

/**
 * @author Bruno Salmon
 */
public class DisplaySelection {

    private final int selectedRow;
    private final int[] selectedRows;

    public DisplaySelection(int[] selectedRows) {
        this(selectedRows.length == 0 ? -1 : selectedRows[0], selectedRows);
    }

    public DisplaySelection(int selectedRow, int[] selectedRows) {
        this.selectedRow = selectedRow;
        this.selectedRows = selectedRows;
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int[] getSelectedRows() {
        return selectedRows;
    }

}
