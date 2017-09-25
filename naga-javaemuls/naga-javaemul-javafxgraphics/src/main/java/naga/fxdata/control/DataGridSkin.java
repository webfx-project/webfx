package naga.fxdata.control;

import emul.javafx.geometry.HPos;
import emul.javafx.geometry.Insets;
import emul.javafx.geometry.VPos;
import emul.javafx.scene.Node;
import emul.javafx.scene.control.ScrollPane;
import emul.javafx.scene.layout.Background;
import emul.javafx.scene.layout.BackgroundFill;
import emul.javafx.scene.layout.Pane;
import emul.javafx.scene.layout.Region;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.shape.Rectangle;
import naga.commons.scheduler.AnimationFramePass;
import naga.commons.scheduler.Scheduled;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.fxdata.displaydata.DisplayColumn;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplayStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class DataGridSkin extends SelectableDisplayResultSetControlSkinBase<DataGrid, Pane, Pane> {

    private final GridHead head = new GridHead();
    private final GridBody body = new GridBody();
    private final ScrollPane bodyScrollPane = new ScrollPane(body);
    private final static Pane fakeCell = new Pane();
    private List<Node> fakeCellChildren;

    private final static double rowHeight = 38;
    private final static double headerHeight = rowHeight;

    public DataGridSkin(DataGrid dataGrid) {
        super(dataGrid, false);
        dataGrid.getStyleClass().add("grid");
/* not yet implemented in gwt
        bodyScrollPane.hvalueProperty().addListener((observable, oldValue, newValue) -> {
                    double hmin = bodyScrollPane.getHmin();
                    double hmax = bodyScrollPane.getHmax();
                    double hvalue = bodyScrollPane.getHvalue();
                    double contentWidth = body.getLayoutBounds().getWidth();
                    double viewportWidth = bodyScrollPane.getViewportBounds().getWidth();
                    double hoffset = Math.max(0, contentWidth - viewportWidth) * (hvalue - hmin) / (hmax - hmin);
                    head.relocate(-hoffset, 0);
                }
        );
*/
        bodyScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        Properties.runNowAndOnPropertiesChange(p -> {
            if (dataGrid.isHeaderVisible())
                getChildren().setAll(head, bodyScrollPane);
            else
                getChildren().setAll(bodyScrollPane);
        }, dataGrid.headerVisibleProperty());
        start();
    }

    @Override
    protected void startBuildingGrid() {
        head.startBuildingGrid();
        body.startBuildingGrid();
    }

    @Override
    protected void buildRowCells(Pane bodyRow, int rowIndex) {
        if (getRowCount() <= 20)
            super.buildRowCells(bodyRow, rowIndex);
    }

    @Override
    protected void endBuildingGrid() {
        if (getRowCount() <= 20) {
            head.endBuildingGrid();
            body.endBuildingGrid();
        } else
            Toolkit.get().scheduler().schedulePeriodicInAnimationFrame(new Consumer<Scheduled>() {
                final DisplayResultSet rs = getRs();
                int rowIndex = 0;
                @Override
                public void accept(Scheduled scheduled) {
                    if (rs != getRs())
                        scheduled.cancel();
                    else {
                        if (rowIndex >= getRowCount())
                            scheduled.cancel();
                        else {
                            DataGridSkin.super.buildRowCells(null, rowIndex);
                        }
                        if (rowIndex == 0) {
                            head.endBuildingGrid();
                            body.endBuildingGrid();
                        }
                        rowIndex++;
                    }
                }
            }, AnimationFramePass.UI_UPDATE_PASS);
    }

    @Override
    protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, DisplayColumn displayColumn) {
        head.getOrCreateHeadColumn(gridColumnIndex).setDisplayColumn(displayColumn);
        body.getOrCreateBodyColumn(gridColumnIndex).setDisplayColumn(displayColumn);
        super.setUpGridColumn(gridColumnIndex, rsColumnIndex, displayColumn);
    }

    @Override
    protected Pane getOrAddHeadCell(int gridColumnIndex) {
        return head.getOrAddHeadCell(gridColumnIndex);
    }

    @Override
    protected Pane getOrAddBodyRow(int rowIndex) {
        return body.getOrAddBodyRow(rowIndex);
    }

    @Override
    protected void applyBodyRowStyleAndBackground(Pane bodyRow, int rowIndex) {
        body.applyBodyRowStyleAndBackground(bodyRow, rowIndex);
    }

    @Override
    protected void applyBodyRowStyleAndBackground(Pane bodyRow, int rowIndex, String rowStyle, Paint rowBackground) {
    }

    @Override
    protected Pane getOrAddBodyRowCell(Pane bodyRow, int rowIndex, int gridColumnIndex) {
        return body.getOrAddBodyRowCell(gridColumnIndex);
    }

    @Override
    protected void setCellContent(Pane cell, Node content, DisplayColumn displayColumn) {
        if (content != null) {
            List<Node> children = cell == fakeCell ? fakeCellChildren : cell.getChildren();
            children.add(content);
        }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 0;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 0;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return Double.MAX_VALUE;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return (getSkinnable().isHeaderVisible() ? headerHeight : 0) + rowHeight * getRowCount() + topInset + bottomInset + 2;
    }

    @Override
    protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return -1;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        updateColumnWidths(contentWidth);
        if (getSkinnable().isHeaderVisible()) {
            layoutInArea(head, contentX, contentY, columnWidthsTotal, headerHeight, -1, HPos.LEFT, VPos.TOP);
            contentY += headerHeight;
            contentHeight -= headerHeight;
        }
        layoutInArea(bodyScrollPane, contentX, contentY, columnWidthsTotal, contentHeight, -1, HPos.LEFT, VPos.TOP);
    }

    private double lastContentWidth;
    private double columnWidthsTotal;

    private void updateColumnWidths(double contentWidth) {
        if (lastContentWidth != contentWidth) {
            columnWidthsTotal = contentWidth;
            List<GridColumn> headColumns = head.headColumns;
            int columnCount = headColumns.size();
            int resizeableColumnCount = columnCount;
            double resizableColumnWidthsTotal = columnWidthsTotal;
            for (GridColumn headColumn : headColumns) {
                Double fixedWidth = headColumn.fixedWidth;
                if (fixedWidth != null) {
                    if (fixedWidth > 24)
                        fixedWidth *= 1.3;
                    fixedWidth = snapSize(fixedWidth + 10); // because of the 5px left and right padding
                    headColumn.setColumnWidth(fixedWidth);
                    resizableColumnWidthsTotal -= fixedWidth;
                    resizeableColumnCount--;
                }
            }
            double resizableColumnWidth = snapSize(resizableColumnWidthsTotal / resizeableColumnCount);
            if (resizeableColumnCount > 0) {
                int resizeableColumnIndex = 0;
                for (GridColumn headColumn : headColumns)
                    if (headColumn.fixedWidth == null) {
                        if (++resizeableColumnIndex < resizeableColumnCount)
                            headColumn.setColumnWidth(resizableColumnWidth);
                        else
                            headColumn.setColumnWidth(resizableColumnWidthsTotal - (resizeableColumnCount - 1) * resizableColumnWidth);
                    }
            }
            for (int i = 0; i < columnCount; i++)
                body.bodyColumns.get(i).setColumnWidth(headColumns.get(i).getColumnWidth());
            body.setPrefWidth(columnWidthsTotal);
            lastContentWidth = contentWidth;
        }
    }

    private class GridHead extends Region {

        private final List<GridColumn> headColumns = new ArrayList<>();

        GridHead() {
            getStyleClass().add("grid-head");
        }

        void startBuildingGrid() {
            headColumns.clear();
        }

        void endBuildingGrid() {
            getChildren().setAll(headColumns);
        }

        Pane getOrAddHeadCell(int gridColumnIndex) {
            fakeCellChildren = getOrCreateHeadColumn(gridColumnIndex).getChildren();
            return fakeCell;
        }

        private GridColumn getOrCreateHeadColumn(int columnIndex) {
            GridColumn gridColumn;
            if (columnIndex < headColumns.size())
                gridColumn = headColumns.get(columnIndex);
            else {
                headColumns.add(gridColumn = new GridColumn(columnIndex));
                lastContentWidth = -1;
            }
            return gridColumn;
        }

        @Override
        protected void layoutChildren() {
            boolean isJavaFxScrollPane = bodyScrollPane.getSkin() != null;
            double x = isJavaFxScrollPane ? 1 : 0;
            double height = rowHeight;
            for (GridColumn headColumn : headColumns) {
                double columnWidth = headColumn.getColumnWidth();
                headColumn.resizeRelocate(x, 0, columnWidth, height);
                if (isJavaFxScrollPane)
                    headColumn.setClip(new Rectangle(columnWidth, height));
                x += columnWidth;
            }
        }
    }

    private class GridBody extends Region {
        private final List<Pane> bodyRows = new ArrayList<>();
        private final List<GridColumn> bodyColumns = new ArrayList<>();

        void startBuildingGrid() {
            bodyRows.clear();
            bodyColumns.clear();
        }

        void endBuildingGrid() {
            List<Node> rowsAndColumns = new ArrayList<>(bodyRows.size() + bodyColumns.size());
            rowsAndColumns.addAll(bodyRows);
            rowsAndColumns.addAll(bodyColumns);
            getChildren().setAll(rowsAndColumns);
            //setPrefWidth(getGridColumnCount() * columnWidth);
            setPrefHeight(getRowCount() * rowHeight);
        }

        private GridColumn getOrCreateBodyColumn(int columnIndex) {
            GridColumn gridColumn;
            if (columnIndex < bodyColumns.size())
                gridColumn = bodyColumns.get(columnIndex);
            else {
                bodyColumns.add(gridColumn = new GridColumn(columnIndex));
                gridColumn.setOnMouseClicked(e -> {
                    int rowIndex = (int) (e.getY() / rowHeight);
                    Pane row = Collections.get(bodyRows, rowIndex);
                    if (row != null)
                        row.getOnMouseClicked().handle(e);
                });
                lastContentWidth = -1;
            }
            return gridColumn;
        }

        Pane getOrAddBodyRow(int rowIndex) {
            Pane bodyRow;
            if (rowIndex < bodyRows.size())
                bodyRow = bodyRows.get(rowIndex);
            else {
                bodyRow = new Pane();
                bodyRow.relocate(0, rowIndex * rowHeight);
                //bodyRow.resize(BIG_WIDTH, rowHeight);
                bodyRow.getStyleClass().add("grid-row");
                bodyRows.add(bodyRow);
            }
            return bodyRow;
        }

        Pane getOrAddBodyRowCell(int gridColumnIndex) {
            return getOrCreateBodyColumn(gridColumnIndex).getOrAddBodyRowCell(gridColumnIndex);
        }

        void applyBodyRowStyleAndBackground(Pane bodyRow, int rowIndex) {
            Object[] rowStyleClasses = getRowStyleClasses(rowIndex);
            if (rowStyleClasses != null) {
                for (Object rowStyleClass : rowStyleClasses)
                    if (rowStyleClass != null)
                        bodyRow.getStyleClass().add(rowStyleClass.toString());
            }
            Paint fill = getRowBackground(rowIndex);
            bodyRow.setBackground(fill == null ? null : new Background(new BackgroundFill(fill, null, null)));
            //bodyRow.setBackground(new Background(new BackgroundFill(Color.YELLOW, null, null)));
        }

        @Override
        protected void layoutChildren() {
            double width = getWidth();
            for (Pane row : bodyRows)
                row.resize(width, rowHeight);
            double x = 0;
            double height = rowHeight * getRowCount();
            for (GridColumn bodyColumn : bodyColumns) {
                double columnWidth = bodyColumn.getColumnWidth();
                bodyColumn.resizeRelocate(x, 0, columnWidth, height);
                bodyColumn.setClip(new Rectangle(columnWidth, height));
                x += columnWidth;
            }
        }
    }

    private static Insets CELL_MARGIN = new Insets(5);

    private class GridColumn extends Pane {
        private Double fixedWidth;
        private HPos hAlignment = HPos.LEFT;
        private VPos vAlignment = VPos.CENTER;
        private double columnWidth;

        GridColumn(int columnIndex) {
            //this.columnIndex = columnIndex;
            //setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
            getStyleClass().add("grid-col");
        }

        void setColumnWidth(double width) {
            columnWidth = width;
        }

        public double getColumnWidth() {
            return columnWidth;
        }

        public void setDisplayColumn(DisplayColumn displayColumn) {
            DisplayStyle style = displayColumn.getStyle();
            if (style != null) {
                fixedWidth = style.getPrefWidth();
                String textAlign = style.getTextAlign();
                if (textAlign != null)
                    switch (textAlign) {
                        case "left":   hAlignment = HPos.LEFT;   break;
                        case "center": hAlignment = HPos.CENTER; break;
                        case "right":  hAlignment = HPos.RIGHT;  break;
                    }
            }
        }

        Pane getOrAddBodyRowCell(int gridColumnIndex) {
            fakeCellChildren = getChildren();
            return fakeCell;
        }


        @Override
        protected void layoutChildren() {
            //System.out.println("Column " + columnIndex + " - layoutChildren() with " + getChildren().size() + " children");
            boolean snapToPixel = getSkinnable().isSnapToPixel();
            double cellWidth = getWidth();
            double cellHeight = rowHeight;
            double y = 0;
            for (Node child : getChildren()) {
                layoutInArea(child, 0, y, cellWidth, cellHeight, -1, CELL_MARGIN, false, false, hAlignment, vAlignment, snapToPixel);
                y += rowHeight;
            }
        }
    }

}
