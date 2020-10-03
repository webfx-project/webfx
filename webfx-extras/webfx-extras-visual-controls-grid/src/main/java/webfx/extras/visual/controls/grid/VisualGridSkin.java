package webfx.extras.visual.controls.grid;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import webfx.extras.visual.*;
import webfx.extras.visual.controls.SelectableVisualResultControlSkinBase;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.scheduler.Scheduled;
import webfx.platform.shared.util.collection.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class VisualGridSkin extends SelectableVisualResultControlSkinBase<VisualGrid, Pane, Pane> {

    private final GridHead gridHead = new GridHead();
    private final GridBody gridBody = new GridBody();
    private ScrollPane bodyScrollPane;
    private Region body;
    private double headOffset;
    private final static Pane fakeCell = new Pane();
    private List<Node> fakeCellChildren;

    private final static double rowHeight = 24;
    private final static double headerHeight = rowHeight;

    VisualGridSkin(VisualGrid visualGrid) {
        super(visualGrid, false);
        visualGrid.getStyleClass().add("grid");
        clipChildren(gridBody);
        gridBody.setBackground(new Background(new BackgroundFill(Color.grayRgb(245), null, null)));
        Properties.runNowAndOnPropertiesChange(() -> {
            if (visualGrid.isFullHeight()) {
                if (bodyScrollPane != null)
                    bodyScrollPane.setContent(null);
                body = gridBody;
            } else {
                if (!(body instanceof ScrollPane)) {
                    if (bodyScrollPane == null) {
                        bodyScrollPane = new ScrollPane();
                        bodyScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                        bodyScrollPane.hvalueProperty().addListener((observable, oldValue, newValue) -> {
                                // Same code as LayoutUtil.computeScrollPaneHoffset() - but not accessible from here
                                double hmin = bodyScrollPane.getHmin();
                                double hmax = bodyScrollPane.getHmax();
                                double hvalue = bodyScrollPane.getHvalue();
                                double contentWidth = gridBody.getLayoutBounds().getWidth();
                                double viewportWidth = bodyScrollPane.getViewportBounds().getWidth();
                                headOffset = Math.max(0, contentWidth - viewportWidth) * (hvalue - hmin) / (hmax - hmin);
                                gridHead.relocate(-headOffset, 0);
                            }
                        );
                    }
                    bodyScrollPane.setContent(gridBody);
                    body = bodyScrollPane;
                }
            }
            if (visualGrid.isHeaderVisible())
                getChildren().setAll(gridHead, body);
            else
                getChildren().setAll(body);
        }, visualGrid.headerVisibleProperty(), visualGrid.fullHeightProperty());
        start();
    }

    private static void clipChildren(Region region) {
        Rectangle outputClip = new Rectangle();
        region.setClip(outputClip);
        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

    @Override
    protected void startBuildingGrid() {
        gridHead.startBuildingGrid();
        gridBody.startBuildingGrid();
    }

    @Override
    protected void buildRowCells(Pane bodyRow, int rowIndex) {
        if (getRowCount() <= 20)
            super.buildRowCells(bodyRow, rowIndex);
    }

    @Override
    protected void endBuildingGrid() {
        if (getRowCount() <= 20) {
            gridHead.endBuildingGrid();
            gridBody.endBuildingGrid();
        } else
            UiScheduler.schedulePeriodicInAnimationFrame(new Consumer<Scheduled/*GWT*/>() {
                final VisualResult rs = getRs();
                int rowIndex = 0;
                @Override
                public void accept(Scheduled scheduled) {
                    if (rs != getRs())
                        scheduled.cancel();
                    else {
                        if (rowIndex >= getRowCount())
                            scheduled.cancel();
                        else {
                            VisualGridSkin.super.buildRowCells(null, rowIndex);
                        }
                        if (rowIndex == 0) {
                            gridHead.endBuildingGrid();
                            gridBody.endBuildingGrid();
                        }
                        rowIndex++;
                    }
                }
            });
    }

    @Override
    protected void setUpGridColumn(int gridColumnIndex, int rsColumnIndex, VisualColumn visualColumn) {
        GridColumn headColumn = gridHead.getOrCreateHeadColumn(gridColumnIndex);
        headColumn.setVisualColumn(visualColumn);
        GridColumn bodyColumn = gridBody.getOrCreateBodyColumn(gridColumnIndex);
        bodyColumn.setVisualColumn(visualColumn);
        if (bodyColumn.cumulator == null)
            bodyColumn.setCumulator(headColumn.getCumulator());
        super.setUpGridColumn(gridColumnIndex, rsColumnIndex, visualColumn);
    }

    @Override
    protected Pane getOrAddHeadCell(int gridColumnIndex) {
        return gridHead.getOrAddHeadCell(gridColumnIndex);
    }

    @Override
    protected Pane getOrAddBodyRow(int rowIndex) {
        return gridBody.getOrAddBodyRow(rowIndex);
    }

    @Override
    protected void applyBodyRowStyleAndBackground(Pane bodyRow, int rowIndex) {
        gridBody.applyBodyRowStyleAndBackground(bodyRow, rowIndex);
    }

    @Override
    protected void applyBodyRowStyleAndBackground(Pane bodyRow, int rowIndex, String rowStyle, Paint rowBackground) {
    }

    @Override
    protected Pane getOrAddBodyRowCell(Pane bodyRow, int rowIndex, int gridColumnIndex) {
        return gridBody.getOrAddBodyRowCell(gridColumnIndex);
    }

    @Override
    protected void setCellContent(Pane cell, Node content, VisualColumn visualColumn) {
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
        double width = leftInset + rightInset;
        List<GridColumn> headColumns = gridHead.headColumns;
        List<GridColumn> bodyColumns = gridBody.bodyColumns;
        int columnCount = headColumns.size();
        for (int i = 0; i < columnCount; i++) {
            GridColumn headColumn = headColumns.get(i);
            if (headColumn.fixedWidth != null)
                width += headColumn.fixedWidth;
            else {
                GridColumn bodyColumn = bodyColumns.get(i);
                ColumnWidthCumulator cumulator = bodyColumn.getUpToDateCumulator();
                double columnWidth = snapSizeX(cumulator.getMaxWidth() + 10); // because of the 5px left and right padding
                width += columnWidth;
            }
        }
        //System.out.println("prefWidth: " + width);
        return width;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return (getSkinnable().isHeaderVisible() ? headerHeight : 0) + rowHeight * getRowCount() + topInset + bottomInset;
    }

    @Override
    protected double computeBaselineOffset(double topInset, double rightInset, double bottomInset, double leftInset) {
        return -1;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        updateColumnWidths(contentWidth);
        if (getSkinnable().isHeaderVisible()) {
            layoutInArea(gridHead, contentX - headOffset, contentY, columnWidthsTotal, headerHeight, -1, HPos.LEFT, VPos.TOP);
            contentY += headerHeight;
            contentHeight -= headerHeight;
        }
        layoutInArea(body, contentX, contentY, contentWidth, contentHeight, -1, HPos.LEFT, VPos.TOP);
    }

    private double lastContentWidth;
    private double columnWidthsTotal;

    private void updateColumnWidths(double contentWidth) {
        if (lastContentWidth != contentWidth) {
            columnWidthsTotal = contentWidth;
            List<GridColumn> headColumns = gridHead.headColumns;
            int columnCount = headColumns.size();
            int remainingColumnCount = columnCount;
            double currentColumnWidthsTotal = 0;
            for (GridColumn headColumn : headColumns) {
                Double fixedWidth = headColumn.fixedWidth;
                if (fixedWidth != null) {
                    if (fixedWidth > 24)
                        fixedWidth *= 1.3;
                    fixedWidth = snapSizeX(fixedWidth + 10); // because of the 5px left and right padding
                    headColumn.setColumnWidth(fixedWidth);
                    currentColumnWidthsTotal += fixedWidth;
                    remainingColumnCount--;
                }
            }
            //double resizableColumnWidth = snapSize(remainingColumnWidthsTotal / remainingColumnCount);
            List<GridColumn> bodyColumns = gridBody.bodyColumns;
            if (remainingColumnCount > 0) {
                double currentResizableColumnWidthsTotal = 0;
                for (int i = 0; i < columnCount; i++) {
                    GridColumn headColumn = headColumns.get(i);
                    if (headColumn.fixedWidth == null) {
                        GridColumn bodyColumn = bodyColumns.get(i);
                        ColumnWidthCumulator cumulator = bodyColumn.getUpToDateCumulator();
                        double columnWidth = snapSizeX(cumulator.getMaxWidth() + 10); // because of the 5px left and right padding
                        headColumn.setColumnWidth(columnWidth);
                        currentResizableColumnWidthsTotal += columnWidth;
                    }
                }
                currentColumnWidthsTotal += currentResizableColumnWidthsTotal;
                double remainingColumnWidthsTotal = columnWidthsTotal - currentColumnWidthsTotal;
                if (remainingColumnWidthsTotal > 0) {
                    for (GridColumn headColumn : headColumns)
                        if (headColumn.fixedWidth == null) {
                            double columnWidth = headColumn.getColumnWidth();
                            columnWidth += columnWidth / currentResizableColumnWidthsTotal * remainingColumnWidthsTotal;
                            headColumn.setColumnWidth(columnWidth);
                        }
                } else if (remainingColumnWidthsTotal < 0) {
                    GridColumn largestColumn = null;
                    for (GridColumn headColumn : headColumns)
                        if (headColumn.fixedWidth == null && (largestColumn == null || largestColumn.getColumnWidth() < headColumn.getColumnWidth()))
                            largestColumn = headColumn;
                    if (largestColumn != null)
                        largestColumn.setColumnWidth(Math.max(0, largestColumn.getColumnWidth() + remainingColumnWidthsTotal));
                }
            }
            for (int i = 0; i < columnCount; i++)
                bodyColumns.get(i).setColumnWidth(headColumns.get(i).getColumnWidth());
            gridBody.setPrefWidth(columnWidthsTotal);
            lastContentWidth = contentWidth;
        }
    }

    private final class GridHead extends Region {

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
            return getOrCreateHeadColumn(gridColumnIndex).getOrAddBodyRowCell();
        }

        private GridColumn getOrCreateHeadColumn(int columnIndex) {
            GridColumn gridColumn;
            if (columnIndex < headColumns.size())
                gridColumn = headColumns.get(columnIndex);
            else {
                headColumns.add(gridColumn = new GridColumn());
                lastContentWidth = -1;
            }
            return gridColumn;
        }

        @Override
        protected void layoutChildren() {
            boolean isJavaFxScrollPane = body == bodyScrollPane && bodyScrollPane.getSkin() != null;
            double x = 0; // isJavaFxScrollPane ? 1 : 0;
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

    private final class GridBody extends Region {
        private final List<Pane> bodyRows = new ArrayList<>();
        private final List<GridColumn> bodyColumns = new ArrayList<>();

        GridBody() {
            getStyleClass().add("grid-body");
        }

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
                bodyColumns.add(gridColumn = new GridColumn());
                gridColumn.setOnMouseClicked(e -> {
                    if (getSkinnable().getSelectionMode() != SelectionMode.DISABLED) {
                        int rowIndex = (int) (e.getY() / rowHeight);
                        Pane row = Collections.get(bodyRows, rowIndex);
                        if (row != null)
                            row.getOnMouseClicked().handle(e);
                    }
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
            return getOrCreateBodyColumn(gridColumnIndex).getOrAddBodyRowCell();
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
            double width = columnWidthsTotal;
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

    private static final Insets CELL_MARGIN = Insets.EMPTY; // new Insets(5);

    private final class GridColumn extends Pane {
        private Double fixedWidth;
        private ColumnWidthCumulator cumulator;
        private HPos hAlignment = HPos.LEFT;
        private final VPos vAlignment = VPos.CENTER;
        private double columnWidth;

        GridColumn() {
            getStyleClass().add("grid-col");
        }

        void setColumnWidth(double width) {
            columnWidth = width;
        }

        double getColumnWidth() {
            return columnWidth;
        }

        void setVisualColumn(VisualColumn visualColumn) {
            VisualStyle style = visualColumn.getStyle();
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
            if (fixedWidth == null)
                setCumulator(visualColumn.getCumulator());
        }

        void setCumulator(ColumnWidthCumulator cumulator) {
            this.cumulator = cumulator;
            if (cumulator != null)
                cumulator.registerColumnNodes(getChildren());
        }

        ColumnWidthCumulator getCumulator() {
            if (cumulator == null)
                setCumulator(new ColumnWidthCumulator());
            return cumulator;
        }

        ColumnWidthCumulator getUpToDateCumulator() {
            getCumulator();
            cumulator.update();
            return cumulator;
        }

        Pane getOrAddBodyRowCell() {
            fakeCellChildren = getChildren();
            return fakeCell;
        }

        @Override
        protected void layoutChildren() {
            //System.out.println("Column " + columnIndex + " - layoutChildren() with " + getChildren().size() + " children");
            boolean snapToPixel = getSkinnable().isSnapToPixel();
            double cellWidth = getWidth();
            double y = 0;
            for (Node child : getChildren()) {
                layoutInArea(child, 0, y, cellWidth, rowHeight, -1, CELL_MARGIN, true, true, hAlignment, vAlignment, snapToPixel);
                y += rowHeight;
            }
        }
    }
}
