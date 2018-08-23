package com.onexip.flexboxfx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.*;

/**
 * Created by TB on 11.10.16.
 */
public class FlexBox extends Pane
{
    private SimpleDoubleProperty horizontalSpace = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty verticalSpace = new SimpleDoubleProperty(0);
    private SimpleObjectProperty<FlexBoxDirection> direction = new SimpleObjectProperty<FlexBoxDirection>(FlexBoxDirection.ROW);
    private double computedMinHeight;
    private boolean performingLayout = false;
    private Orientation bias;
    private boolean biasDirty = true;
    private boolean verbose = false;
    private static final String ORDER_CONSTRAINT = "flexbox-order";
    private static final String GROW_CONSTRAINT = "flexbox-grow";

    public boolean isVerbose()
    {
        return verbose;
    }

    public void setVerbose(boolean verbose)
    {
        this.verbose = verbose;
    }

    public FlexBoxDirection getDirection()
    {
        return direction.get();
    }

    public SimpleObjectProperty<FlexBoxDirection> directionProperty()
    {
        return direction;
    }

    public void setDirection(FlexBoxDirection direction)
    {
        this.direction.set(direction);
    }

    public double getHorizontalSpace()
    {
        return horizontalSpace.get();
    }

    public SimpleDoubleProperty horizontalSpaceProperty()
    {
        return horizontalSpace;
    }

    public void setHorizontalSpace(double horizontalSpace)
    {
        this.horizontalSpace.set(horizontalSpace);
    }

    public double getVerticalSpace()
    {
        return verticalSpace.get();
    }

    public SimpleDoubleProperty verticalSpaceProperty()
    {
        return verticalSpace;
    }

    public void setVerticalSpace(double verticalSpace)
    {
        this.verticalSpace.set(verticalSpace);
    }

    private HashMap<Integer, FlexBoxRow> grid = new HashMap<Integer, FlexBoxRow>();


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

    public static int getOrder(Node child)
    {
        Object constraint = getConstraint(child, ORDER_CONSTRAINT);
        if (constraint == null)
        {
            return 0;
        }
        else
        {
            return (int) constraint;
        }
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
    public static void setGrow(Node child, double value)
    {
        setConstraint(child, GROW_CONSTRAINT, value);
    }

    public static double getGrow(Node child)
    {
        Object o = getConstraint(child, GROW_CONSTRAINT);
        if (o == null)
        {
            return 1;
        }
        else
        {
            return (double) o;
        }
    }

    static void setConstraint(Node node, Object key, Object value)
    {
        if (value == null)
        {
            node.getProperties().remove(key);
        }
        else
        {
            node.getProperties().put(key, value);
        }
        if (node.getParent() != null)
        {
            node.getParent().requestLayout();
        }
    }

    static Object getConstraint(Node node, Object key)
    {
        if (node.hasProperties())
        {
            Object value = node.getProperties().get(key);
            if (value != null)
            {
                return value;
            }
        }
        return null;
    }


    @Override
    protected double computeMinHeight(double width)
    {
        //return super.computeMinHeight(width);
        return computedMinHeight;
    }

    @Override
    protected double computePrefHeight(double width)
    {
//        return super.computePrefHeight(width);
        return computedMinHeight;
    }

    @Override
    protected void layoutChildren()
    {
        //System.out.println("layoutChildren");
        //super.layoutChildren();

        //long timeStart = System.nanoTime();

        grid.clear();

        List<FlexBoxItem> nodesList = new ArrayList<FlexBoxItem>();

        /**
         * First we transform all Nodes to a FlexBoxItem for caching purposes.
         */
        for (Node node : getManagedChildren())
        {
            if (node.isVisible())
            {
                nodesList.add(new FlexBoxItem(node));
            }
        }

        if (getDirection().equals(FlexBoxDirection.ROW) || getDirection().equals(FlexBoxDirection.ROW_REVERSE))    //todo:
        {
            layoutChildrenForRowDirection(nodesList);
        }
        else if (getDirection().equals(FlexBoxDirection.COLUMN) || getDirection().equals(FlexBoxDirection.COLUMN_REVERSE))   //todo:
        {
            layoutChildrenForColumnDirection(nodesList);
        }

/*
        long duration = System.nanoTime() - timeStart;
        if (verbose)
        {
            System.out.println(String.format("# layout duration: %d ms", duration / 1000));
        }
*/
    }

    private void layoutChildrenForColumnDirection(List<FlexBoxItem> nodesList)
    {
        double w = getWidth();
        int noNodes = nodesList.size();
        double growWidth = w - getPadding().getLeft() - getPadding().getRight();

        //  double lastX2 = 0;
        int row = 0;
        int i = 0;

/*
        ListIterator<Node> nodeIterator = null;
        if (getDirection().equals(FlexBoxDirection.COLUMN_REVERSE))
        {
            nodeIterator = new ReverseListIterator<Node>(getManagedChildren());
        }
        else
        {
            nodeIterator = getManagedChildren().listIterator();
        }
*/

        for (FlexBoxItem flexBoxItem : nodesList)
        {
            FlexBoxRow flexBoxRow = new FlexBoxRow();
            flexBoxRow.addFlexBoxItem(flexBoxItem);
            addToGrid(row, flexBoxRow);
            row++;
            i++;
        }

        if (verbose)
        {
            System.out.println("grid = " + grid);
        }


        //Rows durchgehen und width berechnen
        double lastY2 = getPadding().getTop();
        i = 0;
        int noGridRows = grid.size();

        //iterate all rows and calculate node sizes and positions
        for (Integer rowIndex : grid.keySet())
        {
            //contains all nodes per row
            FlexBoxRow flexBoxRow = grid.get(rowIndex);
            ArrayList<FlexBoxItem> rowNodes = flexBoxRow.getNodes();
            double rowNodeX2 = 0;
            double lastMaxHeight = 0;
            //iterate node of row
            for (FlexBoxItem flexBoxItem : rowNodes)
            {
                double rowNodeMinWidth = flexBoxItem.node.minWidth(10);
                double rowNodeMaxWidth = flexBoxItem.node.maxWidth(10);
                double rowNodeWidth = Math.min(rowNodeMaxWidth, Math.max(growWidth, rowNodeMinWidth));

                double h = flexBoxItem.node.prefHeight(growWidth);
                flexBoxItem.node.resizeRelocate(rowNodeX2, lastY2, rowNodeWidth, h);

                lastMaxHeight = Math.max(lastMaxHeight, h);

                rowNodeX2 = rowNodeX2 + rowNodeWidth + getHorizontalSpace();
            }

            lastY2 = lastY2 + lastMaxHeight;
            if (i + 1 < noGridRows)
            {
                lastY2 += getVerticalSpace();
            }
            i++;
        }
    }

    private void layoutChildrenForRowDirection(List<FlexBoxItem> nodesList)
    {
        performingLayout = true;

        double w = getWidth();
        double minWidthSum = 0;
        double noNodes = 0;
        noNodes = nodesList.size();
        double growWidth = (w - (getHorizontalSpace() * (noNodes - 1)) - getPadding().getLeft() - getPadding().getRight()) / noNodes;
        int row = 0;
        int i = 0;

        /**
         * Precalculations
         */
        boolean useOrder = false;
        for (FlexBoxItem flexBoxItem : nodesList)
        {
            flexBoxItem.minWidth = flexBoxItem.node.minWidth(10);
            flexBoxItem.order = getOrder(flexBoxItem.node);
            if (flexBoxItem.order != 0)
            {
                useOrder = true;
            }
            flexBoxItem.grow = getGrow(flexBoxItem.node);
        }

        if (useOrder)
        {
            Collections.sort(nodesList, new Comparator<FlexBoxItem>()
            {
                @Override
                public int compare(FlexBoxItem item1, FlexBoxItem item2)
                {
                    return ((Integer) item1.order).compareTo(item2.order);
                }
            });
        }
        else
        {
            nodesList = FXCollections.observableArrayList(nodesList);
        }

        /**
         * Calculate column-row-grid for auto wrapping
         */
        FlexBoxRow flexBoxRow = new FlexBoxRow();
        addToGrid(row, flexBoxRow);


        for (FlexBoxItem flexBoxItem : nodesList)
        {
            double nodeWidth; // = Math.max(growWidth, flexBoxItem.minWidth);
            nodeWidth = flexBoxItem.minWidth;
            minWidthSum += nodeWidth;

            //is there one more node?
            if (i + 1 < noNodes)
            {
                minWidthSum += getHorizontalSpace();
            }

            if ((int) minWidthSum > (int) w)
            {
                row++;
                flexBoxRow = new FlexBoxRow();
                addToGrid(row, flexBoxRow);
                minWidthSum = nodeWidth;
            }
            flexBoxRow.rowMinWidth += flexBoxItem.minWidth;
            flexBoxRow.flexGrowSum += flexBoxItem.grow;
            flexBoxRow.addFlexBoxItem(flexBoxItem);
            i++;
        }

        //iterate rows and calculate width
        double lastY2 = getPadding().getTop();
        i = 0;
        int noGridRows = grid.size();
        flexBoxRow = null;
        /**
         * iterate grid to calculate node sizes and positions
         * iterate grid-rows first
         */
        for (Integer rowIndex : grid.keySet())
        {
            //contains all nodes per row
            flexBoxRow = grid.get(rowIndex);
            ArrayList<FlexBoxItem> rowNodes = flexBoxRow.getNodes();
            int noRowNodes = rowNodes.size();

            double remainingWidth = w - flexBoxRow.rowMinWidth - (getHorizontalSpace() * (noRowNodes - 1)) - getPadding().getLeft() - getPadding().getRight();
            double flexGrowCellWidth = remainingWidth / flexBoxRow.flexGrowSum;

            ListIterator<FlexBoxItem> rowNodexIterator = null;
            /*if (getDirection().equals(FlexBoxDirection.ROW_REVERSE))
            {
                rowNodexIterator = new ReverseListIterator<FlexBoxItem>(rowNodes);
            }
            else*/
            {
                rowNodexIterator = rowNodes.listIterator();
            }

            double rowNodeX2 = getPadding().getLeft();
            double lastMaxHeight = 0;

            //iterate nodes of row
            while (rowNodexIterator.hasNext())
            {
                FlexBoxItem flexBoxItem = rowNodexIterator.next();
                Node rowNode = flexBoxItem.node;

                double rowNodeMinWidth = flexBoxItem.minWidth;
                double rowNodeMaxWidth = rowNode.maxWidth(10);
                double rowNodeStrechtedWidth = rowNodeMinWidth + (flexGrowCellWidth * flexBoxItem.grow);
                double rowNodeWidth = Math.min(rowNodeMaxWidth, Math.max(rowNodeStrechtedWidth, rowNodeMinWidth));

                double h = rowNode.prefHeight(rowNodeWidth);
                rowNode.resizeRelocate(rowNodeX2, lastY2, rowNodeWidth, h);
                lastMaxHeight = Math.max(lastMaxHeight, h);
                rowNodeX2 = rowNodeX2 + rowNodeWidth + getHorizontalSpace();
            }

            lastY2 = lastY2 + lastMaxHeight;
            if (i + 1 < noGridRows)
            {
                lastY2 += getVerticalSpace();
            }
            i++;
        }
        lastY2 += getPadding().getBottom();

        setMinHeight(lastY2);
        setPrefHeight(lastY2);
        setPrefWidth(lastY2);
        computedMinHeight = lastY2;
        performingLayout = false;
    }


    @Override
    public void requestLayout()
    {
        if (performingLayout)
        {
            return;
        }
        biasDirty = true;
        bias = null;
        super.requestLayout();

    }

    private void addToGrid(int row, FlexBoxRow flexBoxRow)
    {
        grid.put(row, flexBoxRow);
    }
}
