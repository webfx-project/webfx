package naga.toolkit.fx.scene.layout.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.toolkit.fx.geometry.HPos;
import naga.toolkit.fx.geometry.Insets;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.layout.Priority;
import naga.toolkit.fx.scene.layout.VBox;
import naga.toolkit.fx.scene.Node;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class VBoxImpl extends BoxImpl implements VBox {


    /********************************************************************
     *  BEGIN static methods
     ********************************************************************/
    private static final String VGROW_CONSTRAINT = "vbox-vgrow";

    public static void setVgrow(Node child, Priority value) {
        setConstraint(child, VGROW_CONSTRAINT, value);
    }

    public static Priority getVgrow(Node child) {
        return (Priority)getConstraint(child, VGROW_CONSTRAINT);
    }

    public static void clearConstraints(Node child) {
        setVgrow(child, null);
        setMargin(child, null);
    }

    /********************************************************************
     *  END static methods
     ********************************************************************/

    public VBoxImpl() {
        super();
    }

    public VBoxImpl(double spacing) {
        super(spacing);
    }

    public VBoxImpl(Node... children) {
        super(children);
    }

    public VBoxImpl(double spacing, Node... children) {
        super(spacing, children);
    }

    private final Property<Boolean> fillWidthProperty = new SimpleObjectProperty<>(true);
    @Override
    public Property<Boolean> fillWidthProperty() {
        return fillWidthProperty;
    }

    @Override
    protected double computeMinWidth(double height) {
        Insets insets = getInsets();
        List<Node>managed = getManagedChildren();
        double contentWidth;
        if (height != -1 && getContentBias() != null) {
            double prefHeights[][] = getAreaHeights(managed, -1, false);
            adjustAreaHeights(managed, prefHeights, height, -1);
            contentWidth = computeMaxMinAreaWidth(managed, marginAccessor, prefHeights[0], false);
        } else
            contentWidth = computeMaxMinAreaWidth(managed, marginAccessor);
        return snapSpace(insets.getLeft()) + contentWidth + snapSpace(insets.getRight());
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets insets = getInsets();
        return snapSpace(insets.getTop()) +
                computeContentHeight(getManagedChildren(), width, true) +
                snapSpace(insets.getBottom());
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets insets = getInsets();
        List<Node>managed = getManagedChildren();
        double contentWidth;
        if (height != -1 && getContentBias() != null) {
            double prefHeights[][] = getAreaHeights(managed, -1, false);
            adjustAreaHeights(managed, prefHeights, height, -1);
            contentWidth = computeMaxPrefAreaWidth(managed, marginAccessor, prefHeights[0], false);
        } else
            contentWidth = computeMaxPrefAreaWidth(managed, marginAccessor);
        return snapSpace(insets.getLeft()) + contentWidth + snapSpace(insets.getRight());
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets insets = getInsets();
        return snapSpace(insets.getTop()) +
                computeContentHeight(getManagedChildren(), width, false) +
                snapSpace(insets.getBottom());
    }

    @Override
    protected void layoutChildren() {
        performingLayout = true;
        List<Node> managed = getManagedChildren();
        Insets insets = getInsets();
        double width = getWidth();
        double height = getHeight();
        double top = snapSpace(insets.getTop());
        double left = snapSpace(insets.getLeft());
        double bottom = snapSpace(insets.getBottom());
        double right = snapSpace(insets.getRight());
        double space = snapSpace(getSpacing());
        HPos hpos = getAlignmentInternal().getHpos();
        VPos vpos = getAlignmentInternal().getVpos();
        boolean isFillWidth = isFillWidth();

        double[][] actualAreaHeights = getAreaHeights(managed, width, false);
        double contentWidth = width - left - right;
        double contentHeight = adjustAreaHeights(managed, actualAreaHeights, height, width);

        double x = left;
        double y = top + computeYOffset(height - top - bottom, contentHeight, vpos);

        for (int i = 0, size = managed.size(); i < size; i++) {
            Node child = managed.get(i);
            layoutInArea(child, x, y, contentWidth, actualAreaHeights[0][i],
                       /* baseline shouldn't matter */actualAreaHeights[0][i],
                    getMargin(child), isFillWidth, true,
                    hpos, vpos);
            y += actualAreaHeights[0][i] + space;
        }
        performingLayout = false;
    }

    private double[][] getAreaHeights(List<Node>managed, double width, boolean minimum) {
        // width could be -1
        double[][] temp = getTempArray(managed.size());
        double insideWidth = width == -1? -1 : width -
                snapSpace(getInsets().getLeft()) - snapSpace(getInsets().getRight());
        boolean isFillWidth = isFillWidth();
        for (int i = 0, size = managed.size(); i < size; i++) {
            Node child = managed.get(i);
            Insets margin = getMargin(child);
            if (minimum) {
                if (insideWidth != -1 && isFillWidth)
                    temp[0][i] = computeChildMinAreaHeight(child, -1, margin, insideWidth);
                else
                    temp[0][i] = computeChildMinAreaHeight(child, -1, margin, -1);
            } else {
                if (insideWidth != -1 && isFillWidth)
                    temp[0][i] = computeChildPrefAreaHeight(child, -1, margin, insideWidth);
                else
                    temp[0][i] = computeChildPrefAreaHeight(child, -1, margin, -1);
            }
        }
        return temp;
    }

    private double adjustAreaHeights(List<Node>managed, double areaHeights[][], double height, double width) {
        Insets insets = getInsets();
        double left = snapSpace(insets.getLeft());
        double right = snapSpace(insets.getRight());

        double contentHeight = sum(areaHeights[0], managed.size()) + (managed.size()-1)*snapSpace(getSpacing());
        double extraHeight = height -
                snapSpace(insets.getTop()) - snapSpace(insets.getBottom()) - contentHeight;

        if (extraHeight != 0) {
            double refWidth = isFillWidth()&& width != -1? width - left - right : -1;
            double remaining = growOrShrinkAreaHeights(managed, areaHeights, Priority.ALWAYS, extraHeight, refWidth);
            remaining = growOrShrinkAreaHeights(managed, areaHeights, Priority.SOMETIMES, remaining, refWidth);
            contentHeight += (extraHeight - remaining);
        }

        return contentHeight;
    }

    private double growOrShrinkAreaHeights(List<Node>managed, double areaHeights[][], Priority priority, double extraHeight, double width) {
        boolean shrinking = extraHeight < 0;
        int adjustingNumber = 0;

        double[] usedHeights = areaHeights[0];
        double[] temp = areaHeights[1];

        if (shrinking) {
            adjustingNumber = managed.size();
            for (int i = 0, size = managed.size(); i < size; i++) {
                Node child = managed.get(i);
                temp[i] = computeChildMinAreaHeight(child, -1, getMargin(child), width);
            }
        } else
            for (int i = 0, size = managed.size(); i < size; i++) {
                Node child = managed.get(i);
                if (getVgrow(child) == priority) {
                    temp[i] = computeChildMaxAreaHeight(child, -1, getMargin(child), width);
                    adjustingNumber++;
                } else
                    temp[i] = -1;
            }

        double available = extraHeight; // will be negative in shrinking case
        outer: while (Math.abs(available) > 1 && adjustingNumber > 0) {
            double portion = snapPortion(available / adjustingNumber); // negative in shrinking case
            for (int i = 0, size = managed.size(); i < size; i++) {
                if (temp[i] == -1)
                    continue;
                double limit = temp[i] - usedHeights[i]; // negative in shrinking case
                double change = Math.abs(limit) <= Math.abs(portion)? limit : portion;
                usedHeights[i] += change;
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

    private double computeContentHeight(List<Node> managedChildren, double width, boolean minimum) {
        return sum(getAreaHeights(managedChildren, width, minimum)[0], managedChildren.size())
                + (managedChildren.size()-1)*snapSpace(getSpacing());
    }
}
