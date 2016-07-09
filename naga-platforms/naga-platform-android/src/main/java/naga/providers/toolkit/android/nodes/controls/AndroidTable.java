package naga.providers.toolkit.android.nodes.controls;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import naga.providers.toolkit.android.nodes.AndroidSelectableDisplayResultSetNode;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.spi.nodes.controls.Table;
import naga.toolkit.properties.markers.SelectionMode;
import naga.providers.toolkit.android.AndroidToolkit;
import naga.commons.util.Strings;
import naga.commons.util.collection.IdentityList;

/**
 * @author Bruno Salmon
 */
public class AndroidTable extends AndroidSelectableDisplayResultSetNode<TableView<Integer>> implements Table<TableView<Integer>> {

    public AndroidTable() {
        super(new TableView<>(AndroidToolkit.currentActivity));
        node.setColumnCount(2);
    }

    @Override
    protected void onNextSelectionMode(SelectionMode selectionMode) {

    }

    @Override
    protected void onNextDisplayResult(DisplayResultSet displayResultSet) {
        node.setColumnCount(displayResultSet.getColumnCount());
        node.setHeaderAdapter(new DisplayResultTableHeaderAdapter(node.getContext(), displayResultSet));
        node.setDataAdapter(new DisplayResultTableDataAdapter(node.getContext(), displayResultSet));
    }

    private static class DisplayResultTableDataAdapter extends TableDataAdapter<Integer> {

        private final DisplayResultSet displayResultSet;

        DisplayResultTableDataAdapter(Context context, DisplayResultSet displayResultSet) {
            super(context, new IdentityList(displayResultSet.getRowCount()));
            this.displayResultSet = displayResultSet;
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            TextView textView = new TextView(getContext());
            textView.setText(Strings.toString(displayResultSet.getValue(rowIndex, columnIndex)));
            return textView;
        }
    }

    private static class DisplayResultTableHeaderAdapter extends TableHeaderAdapter {

        private final DisplayResultSet displayResultSet;

        DisplayResultTableHeaderAdapter(Context context, DisplayResultSet displayResultSet) {
            super(context, displayResultSet.getColumnCount());
            this.displayResultSet = displayResultSet;
        }

        @Override
        public View getHeaderView(int i, ViewGroup viewGroup) {
            TextView textView = new TextView(getContext());
            textView.setText(displayResultSet.getColumns()[i].getName());
            return textView;
        }
    }

}
