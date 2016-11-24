package naga.toolkit.drawing.geometry.impl;

import naga.toolkit.drawing.geometry.Insets;

/**
 * A set of inside offsets for the 4 side of a rectangular area
 */
public class InsetsImpl implements Insets {

    private double top;
    private double right;
    private double bottom;
    private double left;

    @Override
    public final double getTop() { return top; }

    @Override
    public final double getRight() { return right; }

    @Override
    public final double getBottom() { return bottom; }

    @Override
    public final double getLeft() { return left; }

    /**
     * The cached hash code, used to improve performance in situations where
     * we cache gradients, such as in the CSS routines.
     */
    private int hash = 0;

    /**
     * Constructs a new Insets instance with four different offsets.
     *
     * @param top the top offset
     * @param right the right offset
     * @param bottom the bottom offset
     * @param left the left offset
     */
    public InsetsImpl(double top, double right, double bottom, double left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    /**
     * Constructs a new Insets instance with same value for all four offsets.
     *
     * @param topRightBottomLeft the value used for top, bottom, right and left
     * offset
     */
    public InsetsImpl(double topRightBottomLeft) {
        this.top = topRightBottomLeft;
        this.right = topRightBottomLeft;
        this.bottom = topRightBottomLeft;
        this.left = topRightBottomLeft;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof InsetsImpl) {
            InsetsImpl other = (InsetsImpl) obj;
            return top == other.top
                    && right == other.right
                    && bottom == other.bottom
                    && left == other.left;
        } else return false;

    }

    /**
     * Returns a hash code value for the insets.
     * @return a hash code value for the insets.
     */
    @Override public int hashCode() {
        if (hash == 0) {
            long bits = 17L;
            bits = 37L * bits + Double.doubleToLongBits(top);
            bits = 37L * bits + Double.doubleToLongBits(right);
            bits = 37L * bits + Double.doubleToLongBits(bottom);
            bits = 37L * bits + Double.doubleToLongBits(left);
            hash = (int) (bits ^ (bits >> 32));
        }
        return hash;
    }

    /**
     * Returns a string representation for the insets.
     * @return a string representation for the insets.
     */
    @Override public String toString() {
        return "Insets [top=" + top + ", right=" + right + ", bottom="
                + bottom + ", left=" + left + "]";
    }
}
