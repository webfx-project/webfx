package emul.javafx.geometry;

/**
 * A set of values for describing the flow of visual data for a node.
 *
 * @since JavaFX 8.0
 */
public enum NodeOrientation {
    /**
     * Indicates that the node draws from left-to-right.
     */
    LEFT_TO_RIGHT,

    /**
     * Indicates that the node draws from right-to-left.
     */
    RIGHT_TO_LEFT,

    /**
     * Indicates that the node inherits orientation from the parent.
     */
    INHERIT
}
