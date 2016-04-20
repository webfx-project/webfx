package naga.core.spi.gui.android.nodes;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import naga.core.ngui.displayresult.DisplayResult;
import naga.core.spi.gui.android.AndroidToolkit;
import naga.core.spi.gui.nodes.Table;
import naga.core.util.Strings;
import naga.core.util.collection.IdentityList;

/**
 * @author Bruno Salmon
 */
public class AndroidTable extends AndroidDisplayNode<TableView<Integer>> implements Table<TableView<Integer>> {

    public AndroidTable() {
        super(new TableView<>(AndroidToolkit.currentActivity));
        node.setColumnCount(2);
    }

    @Override
    protected void onNextDisplayResult(DisplayResult displayResult) {
        node.setColumnCount(displayResult.getColumnCount());
        node.setHeaderAdapter(new DisplayResultTableHeaderAdapter(node.getContext(), displayResult));
        node.setDataAdapter(new DisplayResultTableDataAdapter(node.getContext(), displayResult));
    }

    private static class DisplayResultTableDataAdapter extends TableDataAdapter<Integer> {

        private final DisplayResult displayResult;

        DisplayResultTableDataAdapter(Context context, DisplayResult displayResult) {
            super(context, new IdentityList(displayResult.getRowCount()));
            this.displayResult = displayResult;
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            TextView textView = new TextView(getContext());
            textView.setText(Strings.toString(displayResult.getValue(rowIndex, columnIndex)));
            return textView;
        }
    }

    private static class DisplayResultTableHeaderAdapter extends TableHeaderAdapter {

        private final DisplayResult displayResult;

        DisplayResultTableHeaderAdapter(Context context, DisplayResult displayResult) {
            super(context, displayResult.getColumnCount());
            this.displayResult = displayResult;
        }

        @Override
        public View getHeaderView(int i, ViewGroup viewGroup) {
            TextView textView = new TextView(getContext());
            textView.setText(Strings.toString(displayResult.getHeaderValues()[i]));
            return textView;
        }
    }

}
