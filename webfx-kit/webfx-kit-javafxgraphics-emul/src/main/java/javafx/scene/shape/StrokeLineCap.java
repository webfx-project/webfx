package javafx.scene.shape;

/**
 * @author Bruno Salmon
 */
public enum StrokeLineCap {

    /**
     * Ends unclosed subpaths and dash segments with a square projection
     * that extends beyond the end of the segment to a distance
     * equal to half of the line width.
     */
    SQUARE,

    /**
     * Ends unclosed subpaths and dash segments with no added decoration.
     */
    BUTT,

    /**
     * Ends unclosed subpaths and dash segments with a round decoration
     * that has a radius equal to half of the width of the pen.
     */
    ROUND
}
