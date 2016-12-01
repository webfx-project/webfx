package naga.toolkit.drawing.scene.layout;

import naga.toolkit.drawing.geometry.Insets;
import naga.toolkit.drawing.geometry.Orientation;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.drawing.scene.layout.impl.FlowPaneImpl;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface FlowPane extends Pane,
        HasOrientationProperty,
        HasHgapProperty,
        HasVgapProperty,
        HasPrefWrapLengthProperty,
        HasAlignmentProperty,
        HasColumnHalignmentProperty,
        HasRowValignmentProperty
{

    /********************************************************************
     *  BEGIN static methods
     ********************************************************************/

    /**
     * Sets the margin for the child when contained by a flowpane.
     * If set, the flowpane will layout it out with the margin space around it.
     * Setting the value to null will remove the constraint.
     * @param child the child node of a flowpane
     * @param value the margin of space around the child
     */
    static void setMargin(Node child, Insets value) {
        FlowPaneImpl.setMargin(child, value);
    }

    /**
     * Returns the child's margin constraint if set.
     * @param child the child node of a flowpane
     * @return the margin for the child or null if no margin was set
     */
    static Insets getMargin(Node child) {
        return FlowPaneImpl.getMargin(child);
    }

    /**
     * Removes all flowpane constraints from the child node.
     * @param child the child node
     */
    static void clearConstraints(Node child) {
        FlowPaneImpl.clearConstraints(child);
    }

    /********************************************************************
     *  END static methods
     ********************************************************************/

    /**
     * Creates a horizontal FlowPane layout with hgap/vgap = 0.
     */
    static FlowPane create() {
        return new FlowPaneImpl();
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap = 0.
     * @param orientation the direction the tiles should flow & wrap
     */
    static FlowPane create(Orientation orientation) {
        return new FlowPaneImpl(orientation);
    }

    /**
     * Creates a horizontal FlowPane layout with the specified hgap/vgap.
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     */
    static FlowPane create(double hgap, double vgap) {
        return new FlowPaneImpl(hgap, vgap);
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap.
     * @param orientation the direction the tiles should flow & wrap
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     */
    static FlowPane create(Orientation orientation, double hgap, double vgap) {
        return new FlowPaneImpl(orientation, hgap, vgap);
    }

    /**
     * Creates a horizontal FlowPane layout with hgap/vgap = 0.
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    static FlowPane create(Node... children) {
        return new FlowPaneImpl(children);
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap = 0.
     * @param orientation the direction the tiles should flow & wrap
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    static FlowPane create(Orientation orientation, Node... children) {
        return new FlowPaneImpl(orientation, children);
    }

    /**
     * Creates a horizontal FlowPane layout with the specified hgap/vgap.
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    static FlowPane create(double hgap, double vgap, Node... children) {
        return new FlowPaneImpl(hgap, vgap, children);
    }

    /**
     * Creates a FlowPane layout with the specified orientation and hgap/vgap.
     * @param orientation the direction the tiles should flow & wrap
     * @param hgap the amount of horizontal space between each tile
     * @param vgap the amount of vertical space between each tile
     * @param children The initial set of children for this pane.
     * @since JavaFX 8.0
     */
    static FlowPane create(Orientation orientation, double hgap, double vgap, Node... children) {
        return new FlowPaneImpl(orientation, hgap, vgap, children);
    }
    
}
