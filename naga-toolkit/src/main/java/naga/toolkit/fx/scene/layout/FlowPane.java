package naga.toolkit.fx.scene.layout;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import naga.toolkit.fx.geometry.*;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.properties.markers.*;

import java.util.ArrayList;
import java.util.List;

import static naga.toolkit.fx.geometry.Orientation.HORIZONTAL;
import static naga.toolkit.fx.geometry.Orientation.VERTICAL;

/**
 * @author Bruno Salmon
 */
public class FlowPane extends Pane implements
        HasOrientationProperty,
        HasHgapProperty,
        HasVgapProperty,
        HasPrefWrapLengthProperty,
        HasAlignmentProperty,
        HasColumnHalignmentProperty,
        HasRowValignmentProperty {

    /********************************************************************
     *  BEGIN static methods
     ********************************************************************/
    private static final String MARGIN_CONSTRAINT = "flowpane-margin";

    /**
     * Sets the margin for the child when contained by a flowpane.
     * If set, the flowpane will layout it out with the margin space around it.
     * Setting the value to null will remove the constraint.
     * @param child the child node of a flowpane
     * @param value the margin of space around the child
     */
    public static void setMargin(Node child, Insets value) {
        setConstraint(child, MARGIN_CONSTRAINT, value);
    }

    /**
     * Returns the child's margin constraint if set.
     * @param child the child node of a flowpane
     * @return the margin for the child or null if no margin was set
     */
    public static Insets getMargin(Node child) {
        return (Insets)getConstraint(child, MARGIN_CONSTRAINT);
    }

    private static final Callback<Node, Insets> marginAccessor = FlowPane::getMargin;

    /**
     * Removes all flowpane constraints from the child node.
     * @param child the child node
     */
    public static void clearConstraints(Node child) {
        setMargin(child, null);
    }

    /********************************************************************
     *  END static methods
     ********************************************************************/

    /**
     * Creates a horizontal FlowPane layout with hgap/vgap = 0.
     */
    public FlowPane() {
        super();
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap = 0.
     * @param orientation the direction the tiles should flow & wrap
     */
    public FlowPane(Orientation orientation) {
        this();
        setOrientation(orientation);
    }

    /**
     * Creates a horizontal FlowPane layout with the specified hgap/vgap.
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     */
    public FlowPane(double hgap, double vgap) {
        this();
        setHgap(hgap);
        setVgap(vgap);
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap.
     * @param orientation the direction the tiles should flow & wrap
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     */
    public FlowPane(Orientation orientation, double hgap, double vgap) {
        this();
        setOrientation(orientation);
        setHgap(hgap);
        setVgap(vgap);
    }

    /**
     * Creates a horizontal FlowPane layout with hgap/vgap = 0.
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    public FlowPane(Node... children) {
        super();
        getChildren().addAll(children);
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap = 0.
     * @param orientation the direction the tiles should flow & wrap
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    public FlowPane(Orientation orientation, Node... children) {
        this();
        setOrientation(orientation);
        getChildren().addAll(children);
    }

    /**
     * Creates a horizontal FlowPane layout with the specified hgap/vgap.
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    public FlowPane(double hgap, double vgap, Node... children) {
        this();
        setHgap(hgap);
        setVgap(vgap);
        getChildren().addAll(children);
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap.
     * @param orientation the direction the tiles should flow & wrap
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    public FlowPane(Orientation orientation, double hgap, double vgap, Node... children) {
        this();
        setOrientation(orientation);
        setHgap(hgap);
        setVgap(vgap);
        getChildren().addAll(children);
    }

    private final Property<Orientation> orientationProperty = new SimpleObjectProperty<>(HORIZONTAL);
    @Override
    public Property<Orientation> orientationProperty() {
        return orientationProperty;
    }

    private final Property<Double> hgapProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> hgapProperty() {
        return hgapProperty;
    }

    private final Property<Double> vgapProperty = new SimpleObjectProperty<>(0d);
    @Override
    public Property<Double> vgapProperty() {
        return vgapProperty;
    }

    private final Property<Double> prefWrapLengthProperty = new SimpleObjectProperty<>(400d);
    @Override
    public Property<Double> prefWrapLengthProperty() {
        return prefWrapLengthProperty;
    }

    private final Property<Pos> alignmentProperty = new SimpleObjectProperty<>(Pos.TOP_LEFT);
    @Override
    public Property<Pos> alignmentProperty() {
        return alignmentProperty;
    }

    private Pos getAlignmentInternal() {
        Pos localPos = getAlignment();
        return localPos == null ? Pos.TOP_LEFT : localPos;
    }

    private final Property<HPos> columnHalignmentProperty = new SimpleObjectProperty<>(HPos.LEFT);
    @Override
    public Property<HPos> columnHalignmentProperty() {
        return columnHalignmentProperty;
    }

    private HPos getColumnHalignmentInternal() {
        HPos localPos = getColumnHalignment();
        return localPos == null ? HPos.LEFT : localPos;
    }

    private final Property<VPos> rowValignmentProperty = new SimpleObjectProperty<>(VPos.CENTER);
    @Override
    public Property<VPos> rowValignmentProperty() {
        return rowValignmentProperty;
    }

    private VPos getRowValignmentInternal() {
        VPos localPos =  getRowValignment();
        return localPos == null ? VPos.CENTER : localPos;
    }

    @Override
    public Orientation getContentBias() {
        return getOrientation();
    }

    @Override
    protected double computeMinWidth(double height) {
        if (getContentBias() == HORIZONTAL) {
            double maxPref = 0;
            for (Node child : getChildren()) {
                if (child.isManaged())
                    maxPref = Math.max(maxPref, child.prefWidth(-1));
            }
            Insets insets = getInsets();
            return insets.getLeft() + snapSize(maxPref) + insets.getRight();
        }
        return computePrefWidth(height);
    }

    @Override
    protected double computeMinHeight(double width) {
        if (getContentBias() == VERTICAL) {
            double maxPref = 0;
            for (Node child : getChildren()) {
                if (child.isManaged())
                    maxPref = Math.max(maxPref, child.prefHeight(-1));
            }
            Insets insets = getInsets();
            return insets.getTop() + snapSize(maxPref) + insets.getBottom();
        }
        return computePrefHeight(width);
    }

    @Override
    protected double computePrefWidth(double forHeight) {
        Insets insets = getInsets();
        if (getOrientation() == HORIZONTAL) {
            // horizontal
            double maxRunWidth = getPrefWrapLength();
            List<Run> hruns = getRuns(maxRunWidth);
            double w = computeContentWidth(hruns);
            w = getPrefWrapLength() > w ? getPrefWrapLength() : w;
            return insets.getLeft() + snapSize(w) + insets.getRight();
        } else {
            // vertical
            double maxRunHeight = forHeight != -1?
                    forHeight - insets.getTop() - insets.getBottom() : getPrefWrapLength();
            List<Run> vruns = getRuns(maxRunHeight);
            return insets.getLeft() + computeContentWidth(vruns) + insets.getRight();
        }
    }

    @Override
    protected double computePrefHeight(double forWidth) {
        Insets insets = getInsets();
        if (getOrientation() == HORIZONTAL) {
            // horizontal
            double maxRunWidth = forWidth != -1?
                    forWidth - insets.getLeft() - insets.getRight() : getPrefWrapLength();
            List<Run> hruns = getRuns(maxRunWidth);
            return insets.getTop() + computeContentHeight(hruns) + insets.getBottom();
        } else {
            // vertical
            double maxRunHeight = getPrefWrapLength();
            List<Run> vruns = getRuns(maxRunHeight);
            double h = computeContentHeight(vruns);
            h = getPrefWrapLength() > h ? getPrefWrapLength() : h;
            return insets.getTop() + snapSize(h) + insets.getBottom();
        }
    }

    @Override
    public void requestLayout() {
        if (!computingRuns)
            runs = null;
        super.requestLayout();
    }

    private List<Run> runs = null;
    private double lastMaxRunLength = -1;
    private boolean computingRuns = false;

    private List<Run> getRuns(double maxRunLength) {
        if (runs == null || maxRunLength != lastMaxRunLength) {
            computingRuns = true;
            lastMaxRunLength = maxRunLength;
            runs = new ArrayList<>();
            double runLength = 0;
            double runOffset = 0;
            Run run = new Run();
            double vgap = snapSpace(this.getVgap());
            double hgap = snapSpace(this.getHgap());

            for (Node child : getChildren()) {
                if (child.isManaged()) {
                    LayoutRect nodeRect = new LayoutRect();
                    nodeRect.node = child;
                    Insets margin = getMargin(child);
                    nodeRect.width = computeChildPrefAreaWidth(child, margin);
                    nodeRect.height = computeChildPrefAreaHeight(child, margin);
                    double nodeLength = getOrientation() == HORIZONTAL ? nodeRect.width : nodeRect.height;
                    if (runLength + nodeLength > maxRunLength && runLength > 0) {
                        // wrap to next run *unless* its the only node in the run
                        normalizeRun(run, runOffset);
                        if (getOrientation() == HORIZONTAL) // horizontal
                            runOffset += run.height + vgap;
                        else // vertical
                            runOffset += run.width + hgap;
                        runs.add(run);
                        runLength = 0;
                        run = new Run();
                    }
                    if (getOrientation() == HORIZONTAL) {
                        // horizontal
                        nodeRect.x = runLength;
                        runLength += nodeRect.width + hgap;
                    } else {
                        // vertical
                        nodeRect.y = runLength;
                        runLength += nodeRect.height + vgap;
                    }
                    run.rects.add(nodeRect);
                }
            }
            // insert last run
            normalizeRun(run, runOffset);
            runs.add(run);
            computingRuns = false;
        }
        return runs;
    }

    private void normalizeRun(Run run, double runOffset) {
        if (getOrientation() == HORIZONTAL) {
            // horizontal
            ArrayList<Node> rownodes = new ArrayList<>();
            run.width = (run.rects.size() - 1) * snapSpace(getHgap());
            for (LayoutRect lrect : run.rects) {
                rownodes.add(lrect.node);
                run.width += lrect.width;
                lrect.y = runOffset;
            }
            run.height = computeMaxPrefAreaHeight(rownodes, marginAccessor, getRowValignment());
            run.baselineOffset = getRowValignment() == VPos.BASELINE?
                    getAreaBaselineOffset(rownodes, marginAccessor, i -> run.rects.get(i).width, run.height, true) : 0;

        } else {
            // vertical
            run.height = (run.rects.size() - 1) * snapSpace(getVgap());
            double maxw = 0;
            for (LayoutRect lrect : run.rects) {
                run.height += lrect.height;
                lrect.x = runOffset;
                maxw = Math.max(maxw, lrect.width);
            }
            run.width = maxw;
            run.baselineOffset = run.height;
        }
    }

    private double computeContentWidth(List<Run> runs) {
        double cwidth = getOrientation() == HORIZONTAL ? 0 : (runs.size()-1)*snapSpace(getHgap());
        for (Run run : runs) {
            if (getOrientation() == HORIZONTAL)
                cwidth = Math.max(cwidth, run.width);
            else // vertical
                cwidth += run.width;
        }
        return cwidth;
    }

    private double computeContentHeight(List<Run> runs) {
        double cheight = getOrientation() == VERTICAL ? 0 : (runs.size()-1)*snapSpace(getVgap());
        for (Run run : runs) {
            if (getOrientation() == VERTICAL)
                cheight = Math.max(cheight, run.height);
            else // horizontal
                cheight += run.height;
        }
        return cheight;
    }

    @Override
    protected void layoutChildren() {
        Insets insets = getInsets();
        double width = getWidth();
        double height = getHeight();
        double top = insets.getTop();
        double left = insets.getLeft();
        double bottom = insets.getBottom();
        double right = insets.getRight();
        double insideWidth = width - left - right;
        double insideHeight = height - top - bottom;

        //REMIND(aim): need to figure out how to cache the runs to avoid over-calculation
        List<Run> runs = getRuns(getOrientation() == HORIZONTAL ? insideWidth : insideHeight);

        // Now that the nodes are broken into runs, figure out alignments
        for (Run run : runs) {
            double xoffset = left + computeXOffset(insideWidth,
                    getOrientation() == HORIZONTAL ? run.width : computeContentWidth(runs),
                    getAlignmentInternal().getHpos());
            double yoffset = top + computeYOffset(insideHeight,
                    getOrientation() == VERTICAL ? run.height : computeContentHeight(runs),
                    getAlignmentInternal().getVpos());
            for (LayoutRect lrect : run.rects) {
//              System.out.println("flowpane.layout: run="+i+" "+run.width+"x"+run.height+" xoffset="+xoffset+" yoffset="+yoffset+" lrect="+lrect);
                double x = xoffset + lrect.x;
                double y = yoffset + lrect.y;
                layoutInArea(lrect.node, x, y,
                        getOrientation() == HORIZONTAL? lrect.width : run.width,
                        getOrientation() == VERTICAL? lrect.height : run.height,
                        run.baselineOffset, getMargin(lrect.node),
                        getColumnHalignmentInternal(), getRowValignmentInternal());
            }
        }
    }

    //REMIND(aim); replace when we get mutable rects
    private static class LayoutRect {
        public Node node;
        double x;
        double y;
        double width;
        double height;

/*
        @Override public String toString() {
            return "LayoutRect node id="+node.getId()+" "+x+","+y+" "+width+"x"+height;
        }
*/
    }

    private static class Run {
        ArrayList<LayoutRect> rects = new ArrayList<>();
        double width;
        double height;
        double baselineOffset;
    }
}
