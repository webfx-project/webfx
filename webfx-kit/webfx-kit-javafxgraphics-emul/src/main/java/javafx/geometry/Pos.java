package javafx.geometry;

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
    TOP_LEFT(VPos.TOP, HPos.LEFT),

    /**
     * Represents positioning on the top vertically and on the center horizontally.
     */
    TOP_CENTER(VPos.TOP, HPos.CENTER),

    /**
     * Represents positioning on the top vertically and on the right horizontally.
     */
    TOP_RIGHT(VPos.TOP, HPos.RIGHT),

    /**
     * Represents positioning on the center vertically and on the left horizontally.
     */
    CENTER_LEFT(VPos.CENTER, HPos.LEFT),

    /**
     * Represents positioning on the center both vertically and horizontally.
     */
    CENTER(VPos.CENTER, HPos.CENTER),

    /**
     * Represents positioning on the center vertically and on the right horizontally.
     */
    CENTER_RIGHT(VPos.CENTER, HPos.RIGHT),

    /**
     * Represents positioning on the bottom vertically and on the left horizontally.
     */
    BOTTOM_LEFT(VPos.BOTTOM, HPos.LEFT),

    /**
     * Represents positioning on the bottom vertically and on the center horizontally.
     */
    BOTTOM_CENTER(VPos.BOTTOM, HPos.CENTER),

    /**
     * Represents positioning on the bottom vertically and on the right horizontally.
     */
    BOTTOM_RIGHT(VPos.BOTTOM, HPos.RIGHT),

    /**
     * Represents positioning on the baseline vertically and on the left horizontally.
     */
    BASELINE_LEFT(VPos.BASELINE, HPos.LEFT),

    /**
     * Represents positioning on the baseline vertically and on the center horizontally.
     */
    BASELINE_CENTER(VPos.BASELINE, HPos.CENTER),

    /**
     * Represents positioning on the baseline vertically and on the right horizontally.
     */
    BASELINE_RIGHT(VPos.BASELINE, HPos.RIGHT);

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
