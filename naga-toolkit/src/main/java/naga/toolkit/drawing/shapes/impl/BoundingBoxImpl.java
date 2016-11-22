package naga.toolkit.drawing.shapes.impl;


import naga.toolkit.drawing.shapes.BoundingBox;
import naga.toolkit.drawing.shapes.Bounds;
import naga.toolkit.drawing.shapes.Point2D;

/**
 * A rectangular bounding box which is used to describe the bounds of a node
 * or other scene graph object.
 */
public class BoundingBoxImpl extends BoundsImpl implements BoundingBox {
    /**
     * Cache the hash code to make computing hashes faster.
     */
    private int hash = 0;

    public BoundingBoxImpl(double minX, double minY, double width, double height) {
        super(minX, minY, width, height);
    }

    @Override
    public boolean isEmpty() {
        return getMaxX() < getMinX() || getMaxY() < getMinY();
    }

    /**
     * {@inheritDoc}
     * The points on the boundary are considered to lie inside the {@code BoundingBox}.
     */
    @Override
    public boolean contains(Point2D p) {
        return p != null && contains(p.getX(), p.getY());
    }

    /**
     * {@inheritDoc}
     * The points on the boundary are considered to lie inside the {@code BoundingBox}.
     */
    @Override
    public boolean contains(double x, double y) {
        return !isEmpty() && x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY();
    }


    /**
     * {@inheritDoc}
     * The points on the boundary are considered to lie inside the {@code BoundingBox}.
     */
    @Override
    public boolean contains(Bounds b) {
        return !((b == null) || b.isEmpty()) && contains(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    }

    /**
     * {@inheritDoc}
     * The points on the boundary are considered to lie inside the {@code BoundingBox}.
     */
    @Override
    public boolean contains(double x, double y, double w, double h) {
        return contains(x, y) && contains(x + w, y + h);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersects(Bounds b) {
        return !((b == null) || b.isEmpty()) && intersects(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
    }

    /**
     * {@inheritDoc}
     */
    @Override public boolean intersects(double x, double y, double w, double h) {
        return !(isEmpty() || w < 0 || h < 0) && (x + w >= getMinX() && y + h >= getMinY() && x <= getMaxX() && y <= getMaxY());
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof BoundingBoxImpl) {
            BoundingBoxImpl other = (BoundingBoxImpl) obj;
            return getMinX() == other.getMinX()
                    && getMinY() == other.getMinY()
                    && getWidth() == other.getWidth()
                    && getHeight() == other.getHeight();
        } else return false;
    }

    /**
     * Returns a hash code value for the object.
     * @return a hash code value for the object.
     */
    @Override public int hashCode() {
        if (hash == 0) {
            long bits = 7L;
            bits = 31L * bits + Double.doubleToLongBits(getMinX());
            bits = 31L * bits + Double.doubleToLongBits(getMinY());
            bits = 31L * bits + Double.doubleToLongBits(getWidth());
            bits = 31L * bits + Double.doubleToLongBits(getHeight());
            hash = (int) (bits ^ (bits >> 32));
        }
        return hash;
    }

    /**
     * Returns a string representation of this {@code BoundingBox}.
     * This method is intended to be used only for informational purposes.
     * The content and format of the returned string might getMary between
     * implementations.
     * The returned string might be empty but cannot be {@code null}.
     */
    @Override public String toString() {
        return "BoundingBox ["
                + "minX:" + getMinX()
                + ", minY:" + getMinY()
                + ", width:" + getWidth()
                + ", height:" + getHeight()
                + ", maxX:" + getMaxX()
                + ", maxY:" + getMaxY()
                + "]";
    }
}
