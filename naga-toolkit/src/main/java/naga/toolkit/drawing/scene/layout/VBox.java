package naga.toolkit.drawing.scene.layout;

import naga.toolkit.drawing.geometry.Insets;
import naga.toolkit.drawing.scene.layout.impl.VBoxImpl;
import naga.toolkit.drawing.scene.Node;
import naga.toolkit.properties.markers.HasFillWidthProperty;

/**
 * @author Bruno Salmon
 */
public interface VBox extends Box,
        HasFillWidthProperty {

    /**
     * Sets the vertical grow priority for the child when contained by an vbox.
     * If set, the vbox will use the priority to allocate additional space if the
     * vbox is resized larger than it's preferred height.
     * If multiple vbox children have the same vertical grow priority, then the
     * extra space will be split evenly between them.
     * If no vertical grow priority is set on a child, the vbox will never
     * allocate it additional vertical space if available.
     * Setting the value to null will remove the constraint.
     *
     * @param child the child of a vbox
     * @param value the horizontal grow priority for the child
     */
    static void setVgrow(Node child, Priority value) {
        VBoxImpl.setVgrow(child, value);
    }

    /**
     * Returns the child's vgrow property if set.
     *
     * @param child the child node of a vbox
     * @return the vertical grow priority for the child or null if no priority was set
     */
    static Priority getVgrow(Node child) {
        return VBoxImpl.getVgrow(child);
    }

    /**
     * Sets the margin for the child when contained by a vbox.
     * If set, the vbox will layout the child so that it has the margin space around it.
     * Setting the value to null will remove the constraint.
     *
     * @param child the child mode of a vbox
     * @param value the margin of space around the child
     */
    static void setMargin(Node child, Insets value) {
        VBoxImpl.setMargin(child, value);
    }

    /**
     * Returns the child's margin property if set.
     *
     * @param child the child node of a vbox
     * @return the margin for the child or null if no margin was set
     */
    static Insets getMargin(Node child) {
        return VBoxImpl.getMargin(child);
    }

    /**
     * Removes all vbox constraints from the child node.
     * @param child the child node
     */
    static void clearConstraints(Node child) {
        VBoxImpl.clearConstraints(child);
    }


    /**
     * Creates a VBox layout with spacing = 0 and alignment at TOP_LEFT.
     */
    static VBox create() {
        return new VBoxImpl();
    }

    /**
     * Creates a VBox layout with the specified spacing between children.
     *
     * @param spacing the amount of vertical space between each child
     */
    static VBox create(double spacing) {
        return new VBoxImpl(spacing);
    }

    /**
     * Creates an VBox layout with spacing = 0.
     *
     * @param children The initial set of children for this pane.
     */
    static VBox create(Node... children) {
        return new VBoxImpl(children);
    }

    /**
     * Creates an VBox layout with the specified spacing between children.
     *
     * @param spacing  the amount of horizontal space between each child
     * @param children The initial set of children for this pane.
     */
    static VBox create(double spacing, Node... children) {
        return new VBoxImpl(spacing, children);
    }
}
