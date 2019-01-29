package javafx.scene.layout;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import webfx.fxkit.mapper.spi.impl.peer.markers.HasFillHeightProperty;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class HBox extends Box implements
        HasFillHeightProperty {

    private double minBaselineComplement = Double.NaN;
    private double prefBaselineComplement = Double.NaN;

    /********************************************************************
     *  BEGIN static methods
     ********************************************************************/
    private static final String HGROW_CONSTRAINT = "hbox-hgrow";

    /**
     * Sets the horizontal grow priority for the child when contained by an hbox.
     * If set, the hbox will use the priority to allocate additional space if the
     * hbox is resized larger than it's preferred width.
     * If multiple hbox children have the same horizontal grow priority, then the
     * extra space will be split evening between them.
     * If no horizontal grow priority is set on a child, the hbox will never
     * allocate it additional horizontal space if available.
     * Setting the value to null will remove the constraint.
     * @param child the child of an hbox
     * @param value the horizontal grow priority for the child
     */
    public static void setHgrow(Node child, Priority value) {
        setConstraint(child, HGROW_CONSTRAINT, value);
    }

    /**
     * Returns the child's hgrow constraint if set.
     * @param child the child node of an hbox
     * @return the horizontal grow priority for the child or null if no priority was set
     */
    public static Priority getHgrow(Node child) {
        return (Priority)getConstraint(child, HGROW_CONSTRAINT);
    }

    /**
     * Removes all hbox constraints from the child node.
     * @param child the child node
     */
    public static void clearConstraints(Node child) {
        setHgrow(child, null);
        setMargin(child, null);
    }

    /********************************************************************
     *  END static methods
     ********************************************************************/

    /**
     * Creates an HBox layout with spacing = 0.
     */
    public HBox() {
        super();
    }

    /**
     * Creates an HBox layout with the specified spacing between children.
     * @param spacing the amount of horizontal space between each child
     */
    public HBox(double spacing) {
        super(spacing);
    }

    /**
     * Creates an HBox layout with spacing = 0.
     * @param children The initial set of children for this pane.
     */
    public HBox(Node... children) {
        super(children);
    }

    /**
     * Creates an HBox layout with the specified spacing between children.
     * @param spacing the amount of horizontal space between each child
     * @param children The initial set of children for this pane.
     */
    public HBox(double spacing, Node... children) {
        super(spacing, children);
    }

    private final Property<Boolean> fillHeightProperty = new SimpleObjectProperty<>(true);
    @Override
    public Property<Boolean> fillHeightProperty() {
        return fillHeightProperty;
    }


    private boolean shouldFillHeight() {
        return isFillHeight() && getAlignmentInternal().getVpos() != VPos.BASELINE;
    }

    @Override
    protected double computeMinWidth(double height) {
        Insets insets = getPadding();
        return snapSpace(insets.getLeft()) +
                computeContentWidth(getManagedChildren(), height, true) +
                snapSpace(insets.getRight());
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets insets = getPadding();
        List<Node>managed = getManagedChildren();
        double contentHeight;
        if (width != -1 && getContentBias() != null) {
            double prefWidths[][] = getAreaWidths(managed, -1, false);
            adjustAreaWidths(managed, prefWidths, width, -1);
            contentHeight = computeMaxMinAreaHeight(managed, marginAccessor, prefWidths[0], getAlignmentInternal().getVpos());
        } else {
            contentHeight = computeMaxMinAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos());
        }
        return snapSpace(insets.getTop()) +
                contentHeight +
                snapSpace(insets.getBottom());
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets insets = getPadding();
        return snapSpace(insets.getLeft()) +
                computeContentWidth(getManagedChildren(), height, false) +
                snapSpace(insets.getRight());
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets insets = getPadding();
        List<Node> managed = getManagedChildren();
        double contentHeight;
        if (width != -1 && getContentBias() != null) {
            double prefWidths[][] = getAreaWidths(managed, -1, false);
            adjustAreaWidths(managed, prefWidths, width, -1);
            contentHeight = computeMaxPrefAreaHeight(managed, marginAccessor, prefWidths[0], getAlignmentInternal().getVpos());
        } else
            contentHeight = computeMaxPrefAreaHeight(managed, marginAccessor, getAlignmentInternal().getVpos());
        return snapSpace(insets.getTop()) +
                contentHeight +
                snapSpace(insets.getBottom());
    }

    private double[][] getAreaWidths(List<Node>managed, double height, boolean minimum) {
        // height could be -1
        double[][] temp = getTempArray(managed.size());
        double insideHeight = height == -1? -1 : height -
                snapSpace(getPadding().getTop()) - snapSpace(getPadding().getBottom());
        boolean shouldFillHeight = shouldFillHeight();
        for (int i = 0, size = managed.size(); i < size; i++) {
            Node child = managed.get(i);
            Insets margin = getMargin(child);
            if (minimum)
                temp[0][i] = computeChildMinAreaWidth(child, getMinBaselineComplement(), margin, insideHeight, shouldFillHeight);
            else
                temp[0][i] = computeChildPrefAreaWidth(child, getPrefBaselineComplement(), margin, insideHeight, shouldFillHeight);
        }
        return temp;
    }

    private double adjustAreaWidths(List<Node>managed, double areaWidths[][], double width, double height) {
        Insets insets = getPadding();
        double top = snapSpace(insets.getTop());
        double bottom = snapSpace(insets.getBottom());

        double contentWidth = sum(areaWidths[0], managed.size()) + (managed.size()-1)*snapSpace(getSpacing());
        double extraWidth = width -
                snapSpace(insets.getLeft()) - snapSpace(insets.getRight()) - contentWidth;

        if (extraWidth != 0) {
            double refHeight = shouldFillHeight() && height != -1? height - top - bottom : -1;
            double remaining = growOrShrinkAreaWidths(managed, areaWidths, Priority.ALWAYS, extraWidth, refHeight);
            remaining = growOrShrinkAreaWidths(managed, areaWidths, Priority.SOMETIMES, remaining, refHeight);
            contentWidth += (extraWidth - remaining);
        }
        return contentWidth;
    }

    private double growOrShrinkAreaWidths(List<Node>managed, double areaWidths[][], Priority priority, double extraWidth, double height) {
        boolean shrinking = extraWidth < 0;
        int adjustingNumber = 0;

        double[] usedWidths = areaWidths[0];
        double[] temp = areaWidths[1];
        boolean shouldFillHeight = shouldFillHeight();

        if (shrinking) {
            adjustingNumber = managed.size();
            for (int i = 0, size = managed.size(); i < size; i++) {
                Node child = managed.get(i);
                temp[i] = computeChildMinAreaWidth(child, getMinBaselineComplement(), getMargin(child), height, shouldFillHeight);
            }
        } else {
            for (int i = 0, size = managed.size(); i < size; i++) {
                Node child = managed.get(i);
                if (getHgrow(child) == priority) {
                    temp[i] = computeChildMaxAreaWidth(child, getMinBaselineComplement(), getMargin(child), height, shouldFillHeight);
                    adjustingNumber++;
                } else
                    temp[i] = -1;
            }
        }

        double available = extraWidth; // will be negative in shrinking case
        outer:while (Math.abs(available) > 1 && adjustingNumber > 0) {
            double portion = snapPortion(available / adjustingNumber); // negative in shrinking case
            for (int i = 0, size = managed.size(); i < size; i++) {
                if (temp[i] == -1)
                    continue;
                double limit = temp[i] - usedWidths[i]; // negative in shrinking case
                double change = Math.abs(limit) <= Math.abs(portion)? limit : portion;
                usedWidths[i] += change;
                available -= change;
                if (Math.abs(available) < 1)
                    break outer;
                if (Math.abs(change) < Math.abs(portion)) {
                    temp[i] = -1;
                    adjustingNumber--;
                }
            }
        }

        return available; // might be negative in shrinking case
    }

    private double computeContentWidth(List<Node> managedChildren, double height, boolean minimum) {
        return sum(getAreaWidths(managedChildren, height, minimum)[0], managedChildren.size())
                + (managedChildren.size()-1)*snapSpace(getSpacing());
    }

    @Override
    public void requestLayout() {
        if (!performingLayout) {
            minBaselineComplement = Double.NaN;
            prefBaselineComplement = Double.NaN;
            super.requestLayout();
        }
    }

    private double getMinBaselineComplement() {
        if (Double.isNaN(minBaselineComplement)) {
            if (getAlignmentInternal().getVpos() == VPos.BASELINE)
                minBaselineComplement = getMinBaselineComplement(getManagedChildren());
            else
                minBaselineComplement = -1;
        }
        return minBaselineComplement;
    }

    private double getPrefBaselineComplement() {
        if (Double.isNaN(prefBaselineComplement)) {
            if (getAlignmentInternal().getVpos() == VPos.BASELINE)
                prefBaselineComplement = getPrefBaselineComplement(getManagedChildren());
            else
                prefBaselineComplement = -1;
        }
        return prefBaselineComplement;
    }

    private double baselineOffset = Double.NaN;

    @Override
    public double getBaselineOffset() {
        List<Node> managed = getManagedChildren();
        if (managed.isEmpty())
            return BASELINE_OFFSET_SAME_AS_HEIGHT;
        if (Double.isNaN(baselineOffset)) {
            VPos vpos = getAlignmentInternal().getVpos();
            if (vpos == VPos.BASELINE) {
                double max = 0;
                for (Node child : managed) {
                    double offset = child.getBaselineOffset();
                    if (offset == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                        baselineOffset = BASELINE_OFFSET_SAME_AS_HEIGHT;
                        break;
                    } else {
                        Insets margin = getMargin(child);
                        double top = margin != null ? margin.getTop() : 0;
                        max = Math.max(max, top + child.getLayoutBounds().getMinY() + offset);
                    }
                }
                baselineOffset = max + snappedTopInset();
            } else
                baselineOffset = BASELINE_OFFSET_SAME_AS_HEIGHT;
        }
        return baselineOffset;
    }

    @Override
    protected void layoutChildren() {
        performingLayout = true;
        List<Node> managed = getManagedChildren();
        Insets insets = getPadding();
        Pos align = getAlignmentInternal();
        HPos alignHpos = align.getHpos();
        VPos alignVpos = align.getVpos();
        double width = getWidth();
        double height = getHeight();
        double top = snapSpace(insets.getTop());
        double left = snapSpace(insets.getLeft());
        double bottom = snapSpace(insets.getBottom());
        double right = snapSpace(insets.getRight());
        double space = snapSpace(getSpacing());
        boolean shouldFillHeight = shouldFillHeight();

        double[][] actualAreaWidths = getAreaWidths(managed, height, false);
        double contentWidth = adjustAreaWidths(managed, actualAreaWidths, width, height);
        double contentHeight = height - top - bottom;

        double x = left + computeXOffset(width - left - right, contentWidth, align.getHpos());
        double y = top;
        double baselineOffset = -1;
        if (alignVpos == VPos.BASELINE) {
            double baselineComplement = getMinBaselineComplement();
            baselineOffset = getAreaBaselineOffset(managed, marginAccessor, i -> actualAreaWidths[0][i],
                    contentHeight, shouldFillHeight, baselineComplement);
        }

        for (int i = 0, size = managed.size(); i < size; i++) {
            Node child = managed.get(i);
            Insets margin = getMargin(child);
            layoutInArea(child, x, y, actualAreaWidths[0][i], contentHeight,
                    baselineOffset, margin, true, shouldFillHeight,
                    alignHpos, alignVpos);
            x += actualAreaWidths[0][i] + space;
        }
        performingLayout = false;
    }
}
