package naga.providers.toolkit.swing.nodes.controls;

import naga.providers.toolkit.swing.nodes.SwingSelectableDisplayResultSetNode;
import naga.providers.toolkit.swing.util.StyleUtil;
import naga.providers.toolkit.swing.util.JGradientLabel;
import naga.toolkit.adapters.grid.GridFiller;
import naga.toolkit.cell.renderers.ImageTextRenderer;
import naga.toolkit.cell.renderers.ValueRenderer;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.properties.markers.SelectionMode;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.controls.Table;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTable extends SwingSelectableDisplayResultSetNode<JScrollPane> implements Table<JScrollPane> {

    private final JTable table;
    private final DisplayTableModel tableModel = new DisplayTableModel();

    public SwingTable() {
        this(createTransparentScrollPane(createTable()));
    }

    public SwingTable(JScrollPane tableScrollPane) {
        super(tableScrollPane);
        table = (JTable) tableScrollPane.getViewport().getView();
        table.setModel(tableModel);
        syncVisualSelectionMode(getSelectionMode());
        displaySelectionProperty().addListener((observable, oldValue, newValue) -> syncVisualDisplaySelection());
        table.getSelectionModel().addListSelectionListener(e -> syncToolkitDisplaySelection());
    }

    private static JTable createTable() {
        JTable table = new JTable();
        table.setGridColor(StyleUtil.tableGridColor);
        table.setRowHeight(36);
        table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return table;
    }

    private static JScrollPane createTransparentScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setOpaque(false);
        scrollPane.setFont(null);
        scrollPane.setBorder(null);
        JViewport viewport = scrollPane.getViewport();
        viewport.setOpaque(false);
        viewport.setFont(null);
        viewport.setBorder(null);
        return scrollPane;
    }

    private boolean syncingDisplaySelection;

    private void syncToolkitDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            setDisplaySelection(DisplaySelection.createRowsSelection(table.getSelectedRows()));
            syncingDisplaySelection = false;
        }
    }

    private void syncVisualDisplaySelection() {
        if (!syncingDisplaySelection) {
            syncingDisplaySelection = true;
            DisplaySelection displaySelection = getDisplaySelection();
            ListSelectionModel selectionModel = table.getSelectionModel();
            selectionModel.clearSelection();
            if (displaySelection != null)
                displaySelection.forEachRow(row -> selectionModel.addSelectionInterval(row, row));
            syncingDisplaySelection = false;
        }
    }

    @Override
    protected void syncVisualSelectionMode(SelectionMode selectionMode) {
        int swingSelectionMode = 0;
        switch (selectionMode) {
            case DISABLED:
            case SINGLE:
                swingSelectionMode = ListSelectionModel.SINGLE_SELECTION;
                break;
            case MULTIPLE:
                swingSelectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
                break;
        }
        table.getSelectionModel().setSelectionMode(swingSelectionMode);
    }


    @Override
    protected void syncVisualDisplayResult(DisplayResultSet rs) {
        gridFiller.initGrid(rs);
        tableModel.fireTableStructureChanged();
        gridFiller.fillGrid(rs);
        table.doLayout();
    }

    private GridFiller<Object> gridFiller = new GridFiller<Object>(null) {
        @Override
        protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
            TableColumn tableColumn = table.getColumnModel().getColumn(gridColumnIndex);
            naga.toolkit.display.Label label = displayColumn.getLabel();
            tableColumn.setHeaderRenderer(createTableCellRenderer(ImageTextRenderer.SINGLETON, displayColumn, true));
            tableColumn.setHeaderValue(new Object[]{label.getIconPath(), label.getText()});
            Double prefWidth = displayColumn.getStyle().getPrefWidth();
            if (prefWidth != null) {
                // Applying same prefWidth transformation as the PolymerTable (trying to
                if (label.getText() != null)
                    prefWidth = prefWidth * 2.75; // factor compared to JavaFx style (temporary hardcoded)
                prefWidth = prefWidth + 10; // because of the 5px left and right padding
                int width = (int) (double) prefWidth;
                tableColumn.setPreferredWidth(width);
                tableColumn.setMinWidth(width);
                tableColumn.setMaxWidth(width);
            }
            tableColumn.setCellRenderer(createTableCellRenderer(displayColumn.getValueRenderer(), displayColumn, false));
        }
    };

    private TableCellRenderer createTableCellRenderer(ValueRenderer valueRenderer, DisplayColumn displayColumn, boolean header) {
        return valueRenderer == null ? null : (jTable, value, isSelected, hasFocus, row, column) -> {
            Component cellComponent;
            if (valueRenderer != ImageTextRenderer.SINGLETON)
                cellComponent = (Component) Toolkit.unwrapToNativeNode(valueRenderer.renderCellValue(value));
            else {
                ImageTextRenderer renderer = ImageTextRenderer.SINGLETON;
                Object[] array = renderer.getAndCheckArray(value);
                JLabel imageLabel = (JLabel) Toolkit.unwrapToNativeNode(renderer.getImage(array));
                Icon icon = imageLabel == null ? null : imageLabel.getIcon();
                JGradientLabel gradientLabel = new JGradientLabel(renderer.getText(array), icon, SwingConstants.CENTER);
                if (header)
                    gradientLabel.setVerticalGradientColors(Color.WHITE, Color.LIGHT_GRAY);
                cellComponent = gradientLabel;
            }
            String textAlign = displayColumn.getStyle().getTextAlign();
            String rowStyle = gridFiller.getRowStyle(row);
            StyleUtil.styleCellComponent(cellComponent, rowStyle, header, textAlign, isSelected);
            return cellComponent;
        };
    }

    private class DisplayTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            DisplayResultSet rs = getDisplayResultSet();
            return rs == null ? 0 : rs.getRowCount();
        }

        @Override
        public int getColumnCount() {
            return gridFiller.getGridColumnCount();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            DisplayResultSet rs = getDisplayResultSet();
            return rs == null ? null : rs.getValue(rowIndex, gridFiller.gridColumnIndexToResultSetColumnIndex(columnIndex));
        }
    }
}
