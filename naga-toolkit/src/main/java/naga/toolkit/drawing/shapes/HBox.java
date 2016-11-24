package naga.toolkit.drawing.shapes;

import naga.toolkit.drawing.shapes.impl.HBoxImpl;
import naga.toolkit.properties.markers.HasAlignmentProperty;
import naga.toolkit.properties.markers.HasFillHeightProperty;
import naga.toolkit.properties.markers.HasSpacingProperty;

/**
 * @author Bruno Salmon
 */
public interface HBox extends Pane,
        HasSpacingProperty,
        HasAlignmentProperty,
        HasFillHeightProperty {

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
    static void setHgrow(Node child, Priority value) {
        HBoxImpl.setHgrow(child, value);
    }

    /**
     * Returns the child's hgrow constraint if set.
     * @param child the child node of an hbox
     * @return the horizontal grow priority for the child or null if no priority was set
     */
    static Priority getHgrow(Node child) {
        return HBoxImpl.getHgrow(child);
    }

    /**
     * Sets the margin for the child when contained by an hbox.
     * If set, the hbox will layout the child with the margin space around it.
     * Setting the value to null will remove the constraint.
     * @param child the child mode of the hbox
     * @param value the margin of space around the child
     */
    static void setMargin(Node child, Insets value) {
        HBoxImpl.setMargin(child, value);
    }

    /**
     * Returns the child's margin constraint if set.
     * @param child the child node of an hbox
     * @return the margin for the child or null if no margin was set
     */
    static Insets getMargin(Node child) {
        return HBoxImpl.getMargin(child);
    }

    /**
     * Removes all hbox constraints from the child node.
     * @param child the child node
     */
    static void clearConstraints(Node child) {
        HBoxImpl.clearConstraints(child);
    }

    /**
     * Creates an HBox layout with spacing = 0.
     */
    static HBox create() {
        return new HBoxImpl();
    }

    /**
     * Creates an HBox layout with the specified spacing between children.
     * @param spacing the amount of horizontal space between each child
     */
    static HBox create(double spacing) {
        return new HBoxImpl(spacing);
    }

    /**
     * Creates an HBox layout with spacing = 0.
     * @param children The initial set of children for this pane.
     */
    static HBox create(Node... children) {
        return new HBoxImpl(children);
    }

    /**
     * Creates an HBox layout with the specified spacing between children.
     * @param spacing the amount of horizontal space between each child
     * @param children The initial set of children for this pane.
     */
    static HBox create(double spacing, Node... children) {
        return new HBoxImpl(spacing, children);
    }


}
