package naga.toolkit.drawing.layout;

import naga.toolkit.drawing.geometry.Insets;
import naga.toolkit.drawing.geometry.Pos;
import naga.toolkit.drawing.layout.impl.BorderPaneImpl;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface BorderPane extends Pane,
        HasTopProperty,
        HasCenterProperty,
        HasBottomProperty,
        HasLeftProperty,
        HasRightProperty {

    /**
     * Sets the alignment for the child when contained by a border pane.
     * If set, will override the border pane's default alignment for the child's position.
     * Setting the value to null will remove the constraint.
     * @param child the child node of a border pane
     * @param value the alignment position for the child
     */
    static void setAlignment(Node child, Pos value) {
        BorderPaneImpl.setAlignment(child, value);
    }

    /**
     * Returns the child's alignment constraint if set.
     * @param child the child node of a border pane
     * @return the alignment position for the child or null if no alignment was set
     */
    static Pos getAlignment(Node child) {
        return BorderPaneImpl.getAlignment(child);
    }

    /**
     * Sets the margin for the child when contained by a border pane.
     * If set, the border pane will lay it out with the margin space around it.
     * Setting the value to null will remove the constraint.
     * @param child the child node of a border pane
     * @param value the margin of space around the child
     */
    static void setMargin(Node child, Insets value) {
        BorderPaneImpl.setMargin(child, value);
    }

    /**
     * Returns the child's margin constraint if set.
     * @param child the child node of a border pane
     * @return the margin for the child or null if no margin was set
     */
    static Insets getMargin(Node child) {
        return BorderPaneImpl.getMargin(child);
    }

    /**
     * Removes all border pane constraints from the child node.
     * @param child the child node
     */
    static void clearConstraints(Node child) {
        BorderPaneImpl.clearConstraints(child);
    }

    /**
     * Creates a BorderPane layout.
     */
    static BorderPane create() {
        return new BorderPaneImpl();
    }

    /**
     * Creates an BorderPane layout with the given Node as the center of the BorderPane.
     * @param center The node to set as the center of the BorderPane.
     * @since JavaFX 8.0
     */
    static BorderPane create(Node center) {
        return new BorderPaneImpl(center);
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
    static BorderPane create(Node center, Node top, Node right, Node bottom, Node left) {
        return new BorderPaneImpl(center, top, right, bottom, left);
    }
    
}
