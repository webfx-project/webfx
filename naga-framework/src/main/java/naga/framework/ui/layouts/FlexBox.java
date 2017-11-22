package naga.framework.ui.layouts;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import naga.util.collection.Collections;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class FlexBox extends Pane {
    private static final String ORDER_CONSTRAINT = "flexbox-order";
    private static final String GROW_CONSTRAINT = "flexbox-grow";
    private final DoubleProperty horizontalSpace = new SimpleDoubleProperty(0);
    private final DoubleProperty verticalSpace = new SimpleDoubleProperty(0);
    private double computedMinHeight;
    private boolean performingLayout;

    public double getHorizontalSpace() {
        return horizontalSpace.get();
    }

    public DoubleProperty horizontalSpaceProperty() {
        return horizontalSpace;
    }

    public void setHorizontalSpace(double horizontalSpace) {
        this.horizontalSpace.set(horizontalSpace);
    }

    public double getVerticalSpace() {
        return verticalSpace.get();
    }

    public DoubleProperty verticalSpaceProperty() {
        return verticalSpace;
    }

    public void setVerticalSpace(double verticalSpace) {
        this.verticalSpace.set(verticalSpace);
    }

    private final Map<Integer, FlexBoxRow> grid = new HashMap<>();


    /**
     * By default, flex items are laid out in the source order.
     * However, the order property controls the order in which they appear in the flex container.
     *
     * @param child the child of an FlexBox
     * @param value the order in which the child appear in the flex container
     */
    public static void setOrder(Node child, int value)
    {
        setConstraint(child, ORDER_CONSTRAINT, value);
    }

    public static int getOrder(Node child) {
        Object constraint = getConstraint(child, ORDER_CONSTRAINT);
        return constraint == null ? 0 : (int) constraint;
    }

    /**
     * This defines the ability for a flex item to grow if necessary.
     * It accepts a unitless value that serves as a proportion.
     * It dictates what amount of the available space inside the flex container the item should take up.
     * If all items have flex-grow set to 1, the remaining space in the container will be distributed equally to all children.
     * If one of the children has a value of 2, the remaining space would take up twice as much space as the others (or it will try to, at least).
     *
     * @param child the child of an FlexBox
     * @param value grow proportion
     */
    public static void setGrow(Node child, double value) {
        setConstraint(child, GROW_CONSTRAINT, value);
    }

    public static double getGrow(Node child) {
        Object o = getConstraint(child, GROW_CONSTRAINT);
        return o == null ? 1 : (double) o;
    }

    private static void setConstraint(Node node, Object key, Object value) {
        if (value == null)
            node.getProperties().remove(key);
        else
            node.getProperties().put(key, value);
        if (node.getParent() != null)
            node.getParent().requestLayout();
    }

    private static Object getConstraint(Node node, Object key) {
        if (node.hasProperties())
            return node.getProperties().get(key);
        return null;
    }


    @Override
    protected double computeMinHeight(double width) {
        //return super.computeMinHeight(width);
        return computedMinHeight;
    }

    @Override
    protected double computePrefHeight(double width) {
//        return super.computePrefHeight(width);
        return computedMinHeight;
    }

    @Override
    protected void layoutChildren() {
        performingLayout = true;
        grid.clear();
        /*
         * First we transform all Nodes to a FlexBoxItem for caching purposes.
         */
        List<FlexBoxItem> flexBoxItems = Collections.filterMap(getManagedChildren(), Node::isVisible, FlexBoxItem::new);

        if (Collections.hasAtLeastOneMatching(flexBoxItems, item -> item.order != 0))
            flexBoxItems.sort(Collections.comparingInt(item -> item.order));

        /*
         * Calculate column-row-grid for auto wrapping
         */
        int row = 0;
        FlexBoxRow flexBoxRow = new FlexBoxRow();
        addToGrid(row, flexBoxRow);


        double width = getWidth();
        double minWidthSum = 0;

        for (int i = 0, n = flexBoxItems.size(); i < n; i++) {
            FlexBoxItem flexBoxItem = flexBoxItems.get(i);
            double nodeWidth = flexBoxItem.minWidth;
            minWidthSum += nodeWidth;

            //is there one more node?
            if (i + 1 < n)
                minWidthSum += getHorizontalSpace();

            if (minWidthSum > width) {
                addToGrid(++row,  flexBoxRow = new FlexBoxRow());
                minWidthSum = nodeWidth;
            }
            flexBoxRow.addItem(flexBoxItem);
        }

        //iterate rows and calculate width
        double y = getPadding().getTop();
        int i = 0;
        int noGridRows = grid.size();

        /*
         * iterate grid to calculate node sizes and positions
         * iterate grid-rows first
         */
        for (Integer rowIndex : grid.keySet()) {
            //contains all nodes per row
            flexBoxRow = grid.get(rowIndex);
            List<FlexBoxItem> rowItems = flexBoxRow.getItems();
            int noRowItems = rowItems.size();

            double remainingWidth = width - flexBoxRow.rowMinWidth - (getHorizontalSpace() * (noRowItems - 1)) - getPadding().getLeft() - getPadding().getRight();
            double flexGrowCellWidth = remainingWidth / flexBoxRow.flexGrowSum;

            double x = getPadding().getLeft();
            double rowMaxHeight = 0;

            //iterate nodes of row
            for (FlexBoxItem flexBoxItem : rowItems) {
                Node rowNode = flexBoxItem.node;

                double rowNodeMinWidth = flexBoxItem.minWidth;
                double rowNodeMaxWidth = rowNode.maxWidth(-1);
                double rowNodeStrechtedWidth = rowNodeMinWidth + (flexGrowCellWidth * flexBoxItem.grow);
                double rowNodeWidth = snapSize(Math.min(rowNodeMaxWidth, Math.max(rowNodeStrechtedWidth, rowNodeMinWidth)));

                double h = snapSize(rowNode.prefHeight(rowNodeWidth));
                rowNode.resizeRelocate(x, y, rowNodeWidth, h);
                rowMaxHeight = Math.max(rowMaxHeight, h);
                x = snapPosition(x + rowNodeWidth + getHorizontalSpace());
            }

            y += rowMaxHeight;
            if (i + 1 < noGridRows)
                y += getVerticalSpace();
            y = snapPosition(y);
            i++;
        }
        y += getPadding().getBottom();

        setMinHeight(y);
        setPrefHeight(y);
        setPrefWidth(y);
        computedMinHeight = y;
        performingLayout = false;
    }


    @Override
    public void requestLayout() {
        if (!performingLayout)
            super.requestLayout();
    }

    private void addToGrid(int row, FlexBoxRow flexBoxRow) {
        grid.put(row, flexBoxRow);
    }

    private static class FlexBoxItem {
        final Node node;
        final int order;
        final double grow;
        final double minWidth;

        FlexBoxItem(Node node) {
            this.node = node;
            minWidth = node.minWidth(-1);
            order = getOrder(node);
            grow = getGrow(node);
        }
    }

    private static class FlexBoxRow {
        private final ArrayList<FlexBoxItem> items = new ArrayList<>();
        double rowMinWidth;
        double flexGrowSum;

        void addItem(FlexBoxItem flexBoxItem) {
            items.add(flexBoxItem);
            rowMinWidth += flexBoxItem.minWidth;
            flexGrowSum += flexBoxItem.grow;
        }

        ArrayList<FlexBoxItem> getItems() {
            return items;
        }
    }
}
