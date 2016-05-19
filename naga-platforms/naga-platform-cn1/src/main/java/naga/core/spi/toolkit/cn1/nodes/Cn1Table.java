package naga.core.spi.toolkit.cn1.nodes;

import com.codename1.ui.events.DataChangedListener;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.util.EventDispatcher;
import naga.core.ngui.displayresultset.DisplayResultSet;
import naga.core.spi.toolkit.nodes.Table;
import naga.core.util.Strings;


/**
 * @author Bruno Salmon
 */
public class Cn1Table extends Cn1DisplayResultSetNode<com.codename1.ui.table.Table> implements Table<com.codename1.ui.table.Table> {

    private final DisplayTableModel tableModel = new DisplayTableModel();

    public Cn1Table() {
        this(createTable());
    }

    public Cn1Table(com.codename1.ui.table.Table table) {
        super(table);
        table.setModel(tableModel);
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

    /**
     * @author Bruno Salmon
     */
    public static class DisplayTableModel implements TableModel {

        private DisplayResultSet displayResultSet;
        private EventDispatcher dispatcher = new EventDispatcher();

        public void setDisplayResultSet(DisplayResultSet displayResultSet) {
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
            return Strings.toString(displayResultSet.getHeaderValues()[i]);
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
