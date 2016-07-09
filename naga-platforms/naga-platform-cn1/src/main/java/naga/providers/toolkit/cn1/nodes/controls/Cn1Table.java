package naga.providers.toolkit.cn1.nodes.controls;

import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.util.EventDispatcher;
import naga.providers.toolkit.cn1.nodes.Cn1SelectableDisplayResultSetNode;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.display.DisplayResultSet;


/**
 * @author Bruno Salmon
 */
public class Cn1Table extends Cn1SelectableDisplayResultSetNode<com.codename1.ui.table.Table> implements naga.toolkit.spi.nodes.controls.Table<com.codename1.ui.table.Table> {

    private final DisplayTableModel tableModel = new DisplayTableModel();

    public Cn1Table() {
        this(createTable());
    }

    public Cn1Table(com.codename1.ui.table.Table table) {
        super(table);
        table.setModel(tableModel);
    }

    @Override
    protected void onNextSelectionMode(SelectionMode selectionMode) {

    }

    private static com.codename1.ui.table.Table createTable() {
        com.codename1.ui.table.Table table = new com.codename1.ui.table.Table() {
            @Override
            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint con =  super.createCellConstraint(value, row, column);
                con.setWidthPercentage(100 / getModel().getColumnCount());
                return con;
            }
        };
        return table;
    }

    @Override
    protected void onNextDisplayResult(DisplayResultSet displayResultSet) {
        tableModel.setDisplayResultSet(displayResultSet);
        node.setModel(tableModel); // to force refresh
    }

    static class DisplayTableModel implements TableModel {

        private DisplayResultSet displayResultSet;
        private EventDispatcher dispatcher = new EventDispatcher();

        void setDisplayResultSet(DisplayResultSet displayResultSet) {
            this.displayResultSet = displayResultSet;
        }

        @Override
        public int getRowCount() {
            return displayResultSet == null ? 0 : displayResultSet.getRowCount();
        }

        @Override
        public int getColumnCount() {
            return displayResultSet == null ? 0 : displayResultSet.getColumnCount();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return displayResultSet == null ? null : displayResultSet.getValue(rowIndex, columnIndex);
        }

        @Override
        public String getColumnName(int i) {
            return displayResultSet.getColumns()[i].getName();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public void setValueAt(int row, int column, Object o) {

        }

        @Override
        public void addDataChangeListener(DataChangedListener d) {
            dispatcher.addListener(d);
        }

        @Override
        public void removeDataChangeListener(DataChangedListener d) {
            dispatcher.removeListener(d);
        }
    }
}
