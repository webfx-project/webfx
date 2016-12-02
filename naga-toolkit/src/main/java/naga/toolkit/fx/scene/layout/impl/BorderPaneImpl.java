package naga.toolkit.fx.scene.layout.impl;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.collections.ListChangeListener;
import naga.toolkit.fx.geom.Vec2d;
import naga.toolkit.fx.geometry.*;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.Node;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class BorderPaneImpl extends PaneImpl implements BorderPane {
    /********************************************************************
     *  BEGIN static methods
     ********************************************************************/

    private static final String MARGIN = "borderpane-margin";
    private static final String ALIGNMENT = "borderpane-alignment";

    public static void setAlignment(Node child, Pos value) {
        setConstraint(child, ALIGNMENT, value);
    }

    public static Pos getAlignment(Node child) {
        return (Pos)getConstraint(child, ALIGNMENT);
    }

    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN, value);
    }

    public static Insets getMargin(Node child) {
        return (Insets)getConstraint(child, MARGIN);
    }

    // convenience for handling null margins
    private static Insets getNodeMargin(Node child) {
        Insets margin = getMargin(child);
        return margin != null ? margin : Insets.EMPTY;
    }

    public static void clearConstraints(Node child) {
        setAlignment(child, null);
        setMargin(child, null);
    }

    /********************************************************************
     *  END static methods
     ********************************************************************/

    /**
     * Creates a BorderPane layout.
     */
    public BorderPaneImpl() {
        super();
    }

    /**
     * Creates an BorderPane layout with the given Node as the center of the BorderPane.
     * @param center The node to set as the center of the BorderPane.
     * @since JavaFX 8.0
     */
    public BorderPaneImpl(Node center) {
        super();
        setCenter(center);
    }

    /**
     * Creates an BorderPane layout with the given Nodes to use for each of the main
     * layout areas of the Border Pane. The top, right, bottom, and left nodes are listed
     * in clockwise order.
     * @param center The node to set as the center of the BorderPane.
     * @param top The node to set as the top of the BorderPane.
     * @param right The node to set as the right of the BorderPane.
     * @param bottom The node to set as the bottom of the BorderPane.
     * @param left The node to set as the left of the BorderPane.
     * @since JavaFX 8.0
     */
    public BorderPaneImpl(Node center, Node top, Node right, Node bottom, Node left) {
        super();
        setCenter(center);
        setTop(top);
        setRight(right);
        setBottom(bottom);
        setLeft(left);
    }

    /**
     * The node placed in the center of this border pane.
     * If resizable, it will be resized fill the center of the border pane
     * between the top, bottom, left, and right nodes.   If the node cannot be
     * resized to fill the center space (it's not resizable or its max size prevents
     * it) then it will be center aligned unless the child's alignment constraint
     * has been set.
     */
    public final ObjectProperty<Node> centerProperty() {
        if (center == null)
            center = new BorderPositionProperty("center");
        return center;
    }
    private ObjectProperty<Node> center;
    public final void setCenter(Node value) { centerProperty().set(value); }
    public final Node getCenter() { return center == null ? null : center.get(); }

    /**
     * The node placed on the top edge of this border pane.
     * If resizable, it will be resized to its preferred height and it's width
     * will span the width of the border pane.  If the node cannot be
     * resized to fill the top space (it's not resizable or its max size prevents
     * it) then it will be aligned top-left within the space unless the child's
     * alignment constraint has been set.
     */
    public final ObjectProperty<Node> topProperty() {
        if (top == null)
            top = new BorderPositionProperty("top");
        return top;
    }
    private ObjectProperty<Node> top;
    public final void setTop(Node value) { topProperty().set(value); }
    public final Node getTop() { return top == null ? null : top.get();  }

    /**
     * The node placed on the bottom edge of this border pane.
     * If resizable, it will be resized to its preferred height and it's width
     * will span the width of the border pane.  If the node cannot be
     * resized to fill the bottom space (it's not resizable or its max size prevents
     * it) then it will be aligned bottom-left within the space unless the child's
     * alignment constraint has been set.
     */
    public final ObjectProperty<Node> bottomProperty() {
        if (bottom == null)
            bottom = new BorderPositionProperty("bottom");
        return bottom;
    }
    private ObjectProperty<Node> bottom;
    public final void setBottom(Node value) { bottomProperty().set(value); }
    public final Node getBottom() { return bottom == null ? null : bottom.get();  }

    /**
     * The node placed on the left edge of this border pane.
     * If resizable, it will be resized to its preferred width and it's height
     * will span the height of the border pane between the top and bottom nodes.
     * If the node cannot be resized to fill the left space (it's not resizable
     * or its max size prevents it) then it will be aligned top-left within the space
     * unless the child's alignment constraint has been set.
     */
    public final ObjectProperty<Node> leftProperty() {
        if (left == null)
            left = new BorderPositionProperty("left");
        return left;
    }
    private ObjectProperty<Node> left;
    public final void setLeft(Node value) { leftProperty().set(value); }
    public final Node getLeft() { return left == null ? null : left.get(); }

    /**
     * The node placed on the right edge of this border pane.
     * If resizable, it will be resized to its preferred width and it's height
     * will span the height of the border pane between the top and bottom nodes.
     * If the node cannot be resized to fill the right space (it's not resizable
     * or its max size prevents it) then it will be aligned top-right within the space
     * unless the child's alignment constraint has been set.
     */
    public final ObjectProperty<Node> rightProperty() {
        if (right == null)
            right = new BorderPositionProperty("right");
        return right;
    }
    private ObjectProperty<Node> right;
    public final void setRight(Node value) { rightProperty().set(value); }
    public final Node getRight() { return right == null ? null : right.get(); }

    /**
     * @return null unless the center, right, bottom, left or top has a content bias.
     */
    @Override
    public Orientation getContentBias() {
        Node c = getCenter();
        if (c != null && c.isManaged() && c.getContentBias() != null)
            return c.getContentBias();

        Node r = getRight();
        if (r != null && r.isManaged() && r.getContentBias() == Orientation.VERTICAL)
            return r.getContentBias();

        Node l = getLeft();
        if (l != null && l.isManaged() && l.getContentBias() == Orientation.VERTICAL)
            return l.getContentBias();

        Node b = getBottom();
        if (b != null && b.isManaged() && b.getContentBias() == Orientation.HORIZONTAL)
            return b.getContentBias();

        Node t = getTop();
        if (t != null && t.isManaged() && t.getContentBias() == Orientation.HORIZONTAL)
            return t.getContentBias();

        return null;
    }

    @Override
    protected double computeMinWidth(double height) {
        double topMinWidth = getAreaWidth(getTop(), -1, true);
        double bottomMinWidth = getAreaWidth(getBottom(), -1, true);

        double leftPrefWidth;
        double rightPrefWidth;
        double centerMinWidth;

        if (height != -1 && (childHasContentBias(getLeft(), Orientation.VERTICAL) ||
                childHasContentBias(getRight(), Orientation.VERTICAL) ||
                childHasContentBias(getCenter(), Orientation.VERTICAL))) {
            double topPrefHeight = getAreaHeight(getTop(), -1, false);
            double bottomPrefHeight = getAreaHeight(getBottom(), -1, false);

            double middleAreaHeight = Math.max(0, height - topPrefHeight - bottomPrefHeight);

            leftPrefWidth = getAreaWidth(getLeft(), middleAreaHeight, false);
            rightPrefWidth = getAreaWidth(getRight(), middleAreaHeight, false);
            centerMinWidth = getAreaWidth(getCenter(), middleAreaHeight, true);
        } else {
            leftPrefWidth = getAreaWidth(getLeft(), -1, false);
            rightPrefWidth = getAreaWidth(getRight(), -1, false);
            centerMinWidth = getAreaWidth(getCenter(), -1, true);
        }

        Insets insets = getInsets();
        return insets.getLeft() +
                Math.max(leftPrefWidth + centerMinWidth + rightPrefWidth, Math.max(topMinWidth,bottomMinWidth)) +
                insets.getRight();
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets insets = getInsets();

        // Bottom and top are always at their pref height
        double topPrefHeight = getAreaHeight(getTop(), width, false);
        double bottomPrefHeight = getAreaHeight(getBottom(), width, false);

        double leftMinHeight = getAreaHeight(getLeft(), -1, true);
        double rightMinHeight = getAreaHeight(getRight(), -1, true);

        double centerMinHeight;
        if (width != -1 && childHasContentBias(getCenter(), Orientation.HORIZONTAL)) {
            double leftPrefWidth = getAreaWidth(getLeft(), -1, false);
            double rightPrefWidth = getAreaWidth(getRight(), -1, false);
            centerMinHeight = getAreaHeight(getCenter(),
                    Math.max(0, width - leftPrefWidth - rightPrefWidth) , true);
        } else
            centerMinHeight = getAreaHeight(getCenter(), -1, true);

        double middleAreaMinHeigh = Math.max(centerMinHeight, Math.max(rightMinHeight, leftMinHeight));

        return insets.getTop() + topPrefHeight + middleAreaMinHeigh + bottomPrefHeight + insets.getBottom();
    }

    @Override
    protected double computePrefWidth(double height) {
        double topPrefWidth = getAreaWidth(getTop(), -1, false);
        double bottomPrefWidth = getAreaWidth(getBottom(), -1, false);

        double leftPrefWidth;
        double rightPrefWidth;
        double centerPrefWidth;

        if (height != -1 && (childHasContentBias(getLeft(), Orientation.VERTICAL) ||
                childHasContentBias(getRight(), Orientation.VERTICAL) ||
                childHasContentBias(getCenter(), Orientation.VERTICAL))) {
            double topPrefHeight = getAreaHeight(getTop(), -1, false);
            double bottomPrefHeight = getAreaHeight(getBottom(), -1, false);

            double middleAreaHeight = Math.max(0, height - topPrefHeight - bottomPrefHeight);

            leftPrefWidth = getAreaWidth(getLeft(), middleAreaHeight, false);
            rightPrefWidth = getAreaWidth(getRight(), middleAreaHeight, false);
            centerPrefWidth = getAreaWidth(getCenter(), middleAreaHeight, false);
        } else {
            leftPrefWidth = getAreaWidth(getLeft(), -1, false);
            rightPrefWidth = getAreaWidth(getRight(), -1, false);
            centerPrefWidth = getAreaWidth(getCenter(), -1, false);
        }

        Insets insets = getInsets();
        return insets.getLeft() +
                Math.max(leftPrefWidth + centerPrefWidth + rightPrefWidth, Math.max(topPrefWidth,bottomPrefWidth)) +
                insets.getRight();
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets insets = getInsets();

        double topPrefHeight = getAreaHeight(getTop(), width, false);
        double bottomPrefHeight = getAreaHeight(getBottom(), width, false);
        double leftPrefHeight = getAreaHeight(getLeft(), -1, false);
        double rightPrefHeight = getAreaHeight(getRight(), -1, false);

        double centerPrefHeight;
        if (width != -1 && childHasContentBias(getCenter(), Orientation.HORIZONTAL)) {
            double leftPrefWidth = getAreaWidth(getLeft(), -1, false);
            double rightPrefWidth = getAreaWidth(getRight(), -1, false);
            centerPrefHeight = getAreaHeight(getCenter(),
                    Math.max(0, width - leftPrefWidth - rightPrefWidth) , false);
        } else
            centerPrefHeight = getAreaHeight(getCenter(), -1, false);

        double middleAreaPrefHeight = Math.max(centerPrefHeight, Math.max(rightPrefHeight, leftPrefHeight));

        return insets.getTop() + topPrefHeight + middleAreaPrefHeight + bottomPrefHeight + insets.getBottom();
    }

    @Override
    protected void layoutChildren() {
        Insets insets = getInsets();
        double width = getWidth();
        double height = getHeight();
        Orientation bias = getContentBias();

        if (bias == null) {
            double minWidth = minWidth(-1);
            double minHeight = minHeight(-1);
            width = width < minWidth ? minWidth : width;
            height = height < minHeight ? minHeight : height;
        } else if (bias == Orientation.HORIZONTAL) {
            double minWidth = minWidth(-1);
            width = width < minWidth ? minWidth : width;
            double minHeight = minHeight(width);
            height = height < minHeight ? minHeight : height;
        } else {
            double minHeight = minHeight(-1);
            height = height < minHeight ? minHeight : height;
            double minWidth = minWidth(height);
            width = width < minWidth ? minWidth : width;
        }

        double insideX = insets.getLeft();
        double insideY = insets.getTop();
        double insideWidth = width - insideX - insets.getRight();
        double insideHeight = height - insideY - insets.getBottom();
        Node c = getCenter();
        Node r = getRight();
        Node b = getBottom();
        Node l = getLeft();
        Node t = getTop();

        double topHeight = 0;
        if (t != null && t.isManaged()) {
            Insets topMargin = getNodeMargin(t);
            double adjustedWidth = adjustWidthByMargin(insideWidth, topMargin);
            double adjustedHeight = adjustHeightByMargin(insideHeight, topMargin);
            topHeight = snapSize(t.prefHeight(adjustedWidth));
            topHeight = Math.min(topHeight, adjustedHeight);
            Vec2d result = boundedNodeSizeWithBias(t, adjustedWidth,
                    topHeight, true, true, TEMP_VEC2D);
            topHeight = snapSize(result.y);
            t.resize(snapSize(result.x), topHeight);

            topHeight = snapSpace(topMargin.getBottom()) + topHeight + snapSpace(topMargin.getTop());
            Pos alignment = getAlignment(t);
            positionInArea(t, insideX, insideY, insideWidth, topHeight, 0/*ignore baseline*/,
                    topMargin,
                    alignment != null? alignment.getHpos() : HPos.LEFT,
                    alignment != null? alignment.getVpos() : VPos.TOP, isSnapToPixel());
        }

        double bottomHeight = 0;
        if (b != null && b.isManaged()) {
            Insets bottomMargin = getNodeMargin(b);
            double adjustedWidth = adjustWidthByMargin(insideWidth, bottomMargin);
            double adjustedHeight = adjustHeightByMargin(insideHeight - topHeight, bottomMargin);
            bottomHeight = snapSize(b.prefHeight(adjustedWidth));
            bottomHeight = Math.min(bottomHeight, adjustedHeight);
            Vec2d result = boundedNodeSizeWithBias(b, adjustedWidth,
                    bottomHeight, true, true, TEMP_VEC2D);
            bottomHeight = snapSize(result.y);
            b.resize(snapSize(result.x), bottomHeight);

            bottomHeight = snapSpace(bottomMargin.getBottom()) + bottomHeight + snapSpace(bottomMargin.getTop());
            Pos alignment = getAlignment(b);
            positionInArea(b, insideX, insideY + insideHeight - bottomHeight,
                    insideWidth, bottomHeight, 0/*ignore baseline*/,
                    bottomMargin,
                    alignment != null? alignment.getHpos() : HPos.LEFT,
                    alignment != null? alignment.getVpos() : VPos.BOTTOM, isSnapToPixel());
        }

        double leftWidth = 0;
        if (l != null && l.isManaged()) {
            Insets leftMargin = getNodeMargin(l);
            double adjustedWidth = adjustWidthByMargin(insideWidth, leftMargin);
            double adjustedHeight = adjustHeightByMargin(insideHeight - topHeight - bottomHeight, leftMargin); // ????
            leftWidth = snapSize(l.prefWidth(adjustedHeight));
            leftWidth = Math.min(leftWidth, adjustedWidth);
            Vec2d result = boundedNodeSizeWithBias(l, leftWidth, adjustedHeight,
                    true, true, TEMP_VEC2D);
            leftWidth = snapSize(result.x);
            l.resize(leftWidth, snapSize(result.y));

            leftWidth = snapSpace(leftMargin.getLeft()) + leftWidth + snapSpace(leftMargin.getRight());
            Pos alignment = getAlignment(l);
            positionInArea(l, insideX, insideY + topHeight,
                    leftWidth, insideHeight - topHeight - bottomHeight, 0/*ignore baseline*/,
                    leftMargin,
                    alignment != null? alignment.getHpos() : HPos.LEFT,
                    alignment != null? alignment.getVpos() : VPos.TOP, isSnapToPixel());
        }

        double rightWidth = 0;
        if (r != null && r.isManaged()) {
            Insets rightMargin = getNodeMargin(r);
            double adjustedWidth = adjustWidthByMargin(insideWidth - leftWidth, rightMargin);
            double adjustedHeight = adjustHeightByMargin(insideHeight - topHeight - bottomHeight, rightMargin);

            rightWidth = snapSize(r.prefWidth(adjustedHeight));
            rightWidth = Math.min(rightWidth, adjustedWidth);
            Vec2d result = boundedNodeSizeWithBias(r, rightWidth, adjustedHeight,
                    true, true, TEMP_VEC2D);
            rightWidth = snapSize(result.x);
            r.resize(rightWidth, snapSize(result.y));

            rightWidth = snapSpace(rightMargin.getLeft()) + rightWidth + snapSpace(rightMargin.getRight());
            Pos alignment = getAlignment(r);
            positionInArea(r, insideX + insideWidth - rightWidth, insideY + topHeight,
                    rightWidth, insideHeight - topHeight - bottomHeight, 0/*ignore baseline*/,
                    rightMargin,
                    alignment != null? alignment.getHpos() : HPos.RIGHT,
                    alignment != null? alignment.getVpos() : VPos.TOP, isSnapToPixel());
        }

        if (c != null && c.isManaged()) {
            Pos alignment = getAlignment(c);

            layoutInArea(c, insideX + leftWidth, insideY + topHeight,
                    insideWidth - leftWidth - rightWidth,
                    insideHeight - topHeight - bottomHeight, 0/*ignore baseline*/,
                    getNodeMargin(c),
                    alignment != null? alignment.getHpos() : HPos.CENTER,
                    alignment != null? alignment.getVpos() : VPos.CENTER);
        }
    }

    private double getAreaWidth(Node child, double height, boolean minimum) {
        if (child != null && child.isManaged()) {
            Insets margin = getNodeMargin(child);
            return minimum ? computeChildMinAreaWidth(child, -1, margin, height, false):
                    computeChildPrefAreaWidth(child, -1, margin, height, false);
        }
        return 0;
    }

    private double getAreaHeight(Node child, double width, boolean minimum) {
        if (child != null && child.isManaged()) {
            Insets margin = getNodeMargin(child);
            return minimum ? computeChildMinAreaHeight(child, -1, margin, width):
                    computeChildPrefAreaHeight(child, -1, margin, width);
        }
        return 0;
    }

    private boolean childHasContentBias(Node child, Orientation orientation) {
        return child != null && child.isManaged() && child.getContentBias() == orientation;
    }

    /***************************************************************************
     *                                                                         *
     *                         Private Inner Class                             *
     *                                                                         *
     **************************************************************************/

    private final class BorderPositionProperty extends ObjectPropertyBase<Node> {
        private Node oldValue = null;
        private final String propertyName;
        private boolean isBeingInvalidated;

        BorderPositionProperty(String propertyName) {
            this.propertyName = propertyName;
            getChildren().addListener((ListChangeListener<Node>) c -> {
                if (oldValue == null || isBeingInvalidated)
                    return;
                while (c.next()) {
                    if (c.wasRemoved()) {
                        List<? extends Node> removed = c.getRemoved();
                        for (Node aRemoved : removed) {
                            if (aRemoved == oldValue) {
                                oldValue = null; // Do not remove again in invalidated
                                set(null);
                            }
                        }
                    }
                }
            });
        }

        @Override
        protected void invalidated() {
            List<Node> children = getChildren();

            isBeingInvalidated = true;
            try {
                if (oldValue != null)
                    children.remove(oldValue);

                Node _value = get();
                oldValue = _value;

                if (_value != null)
                    children.add(_value);
            } finally {
                isBeingInvalidated = false;
            }
        }

        @Override
        public Object getBean() {
            return BorderPaneImpl.this;
        }

        @Override
        public String getName() {
            return propertyName;
        }
    }
}

