package naga.toolkit.drawing.shapes;

import static naga.toolkit.drawing.shapes.HPos.LEFT;
import static naga.toolkit.drawing.shapes.HPos.RIGHT;
import static naga.toolkit.drawing.shapes.VPos.BASELINE;
import static naga.toolkit.drawing.shapes.VPos.BOTTOM;
import static naga.toolkit.drawing.shapes.VPos.TOP;

/**
 * A set of values for describing vertical and horizontal positioning and
 * alignment.
 *
 * @since JavaFX 2.0
 */
public enum Pos {

    /**
     * Represents positioning on the top vertically and on the left horizontally.
     */
    TOP_LEFT(TOP, LEFT),

    /**
     * Represents positioning on the top vertically and on the center horizontally.
     */
    TOP_CENTER(TOP, HPos.CENTER),

    /**
     * Represents positioning on the top vertically and on the right horizontally.
     */
    TOP_RIGHT(TOP, RIGHT),

    /**
     * Represents positioning on the center vertically and on the left horizontally.
     */
    CENTER_LEFT(VPos.CENTER, LEFT),

    /**
     * Represents positioning on the center both vertically and horizontally.
     */
    CENTER(VPos.CENTER, HPos.CENTER),

    /**
     * Represents positioning on the center vertically and on the right horizontally.
     */
    CENTER_RIGHT(VPos.CENTER, RIGHT),

    /**
     * Represents positioning on the bottom vertically and on the left horizontally.
     */
    BOTTOM_LEFT(BOTTOM, LEFT),

    /**
     * Represents positioning on the bottom vertically and on the center horizontally.
     */
    BOTTOM_CENTER(BOTTOM, HPos.CENTER),

    /**
     * Represents positioning on the bottom vertically and on the right horizontally.
     */
    BOTTOM_RIGHT(BOTTOM, RIGHT),

    /**
     * Represents positioning on the baseline vertically and on the left horizontally.
     */
    BASELINE_LEFT(BASELINE, LEFT),

    /**
     * Represents positioning on the baseline vertically and on the center horizontally.
     */
    BASELINE_CENTER(BASELINE, HPos.CENTER),

    /**
     * Represents positioning on the baseline vertically and on the right horizontally.
     */
    BASELINE_RIGHT(BASELINE, RIGHT);

    private final VPos vpos;
    private final HPos hpos;

    Pos(VPos vpos, HPos hpos) {
        this.vpos = vpos;
        this.hpos = hpos;
    }

    /**
     * Returns the vertical positioning/alignment.
     * @return the vertical positioning/alignment.
     */
    public VPos getVpos() {
        return vpos;
    }

    /**
     * Returns the horizontal positioning/alignment.
     * @return the horizontal positioning/alignment.
     */
    public HPos getHpos() {
        return hpos;
    }
}
