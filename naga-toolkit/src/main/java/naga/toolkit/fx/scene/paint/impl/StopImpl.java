package naga.toolkit.fx.scene.paint.impl;

import naga.toolkit.fx.scene.paint.Color;
import naga.toolkit.fx.scene.paint.Stop;

/**
 * Defines one element of the ramp of colors to use on a gradient.
 * For more information see {@code LinearGradient} and
 * {@code RadialGradient}.
 *
 * <p>Example:</p>
 * <pre><code>
 * // object bounding box relative (proportional:true, default)
 * Stop[] stops = { new Stop(0, Color.WHITE), new Stop(1, Color.BLACK)};
 * LinearGradient lg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.No_CYCLE, stops);
 * Rectangle r = new Rectangle();
 * r.setFill(lg);
 *</code></pre>
 * @since JavaFX 2.0
 */
public final class StopImpl implements Stop {

    /**
     * The {@code offset} variable is a number ranging from {@code 0} to {@code 1}
     * that indicates where this gradient stop is placed. For linear gradients,
     * the {@code offset} variable represents a location along the gradient vector.
     * For radial gradients, it represents a percentage distance from
     * the focus point to the edge of the outermost/largest circle.
     *
     * @profile common
     * @defaultValue 0.0
     */
    private double offset;

    /**
     * Gets a number ranging from {@code 0} to {@code 1}
     * that indicates where this gradient stop is placed. For linear gradients,
     * the {@code offset} variable represents a location along the gradient vector.
     * For radial gradients, it represents a percentage distance from
     * the focus point to the edge of the outermost/largest circle.
     *
     * @return position of the Stop within the gradient
     *         (ranging from {@code 0} to {@code 1})
     */
    @Override
    public final double getOffset() {
        return offset;
    }

    /**
     * The color of the gradient at this offset.
     *
     * @profile common
     * @defaultValue Color.BLACK
     */
    private Color color;

    /**
     * Gets the color of the gradient at this offset.
     * @return the color of the gradient at this offset
     */
    @Override
    public final Color getColor() {
        return color;
    }

    /**
     * The cached hash code, used to improve performance in situations where
     * we cache gradients, such as in the CSS routines.
     */
    private int hash = 0;

    /**
     * Creates a new instance of Stop.
     * @param offset Stop's position (ranging from {@code 0} to {@code 1}
     * @param color Stop's color
     */
    public StopImpl(double offset, Color color) {
        this.offset = offset;
        this.color = color;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is equal to the {@code obj} argument; {@code false} otherwise.
     */
    @Override public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof StopImpl) {
            StopImpl other = (StopImpl) obj;
            return offset == other.offset &&
                    (color == null ? other.color == null : color.equals(other.color));
        } else return false;
    }

    /**
     * Returns a hash code for this {@code Stop} object.
     * @return a hash code for this {@code Stop} object.
     */
    @Override public int hashCode() {
        if (hash == 0) {
            long bits = 17L;
            bits = 37L * bits + Double.doubleToLongBits(offset);
            bits = 37L * bits + color.hashCode();
            hash = (int) (bits ^ (bits >> 32));
        }
        return hash;
    }

    /**
     * Returns a string representation of this {@code Stop} object.
     * @return a string representation of this {@code Stop} object.
     */
    @Override public String toString() {
        return color + " " + offset*100 + "%";
    }
}
