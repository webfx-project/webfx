package naga.toolkit.drawing.geom;

/**
 * A simple object which carries bounds information as doubles, and
 * has no Z components.
 */
public final class RectBounds extends BaseBounds {
    // minimum x value of bounding box
    private double minX;
    // maximum x value of bounding box
    private double maxX;
    // minimum y value of bounding box
    private double minY;
    // maximum y value of bounding box
    private double maxY;

    /**
     * Create an axis aligned bounding rectangle object, with an empty bounds
     * where maxX < minX and maxY < minY.
     */
    public RectBounds() {
        minX = minY = 0.0d;
        maxX = maxY = -1.0d;
    }

    @Override public BaseBounds copy() {
        return new RectBounds(minX, minY, maxX, maxY);
    }

    /**
     * Creates a RectBounds based on the minX, minY, maxX, and maxY values specified.
     */
    public RectBounds(double minX, double minY, double maxX, double maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    /**
     * Creates a RectBounds object as a copy of the specified RectBounds object.
     */
    public RectBounds(RectBounds other) {
        setBounds(other);
    }

    /**
     * Creates a RectBounds object as a copy of the specified RECTANGLE.
     */
/*
    public RectBounds(Rectangle other) {
        setBounds(other.x, other.y,
                other.x + other.width, other.y + other.height);
    }
*/

    @Override public BoundsType getBoundsType() {
        return BoundsType.RECTANGLE;
    }

    @Override public boolean is2D() {
        return true;
    }

    /**
     * Convenience function for getting the width of this RectBounds.
     * The dimension along the X-Axis.
     */
    @Override public double getWidth() {
        return maxX - minX;
    }

    /**
     * Convenience function for getting the height of this RectBounds
     * The dimension along the Y-Axis.
     */
    @Override public double getHeight() {
        return maxY - minY;
    }

    /**
     * Convenience function for getting the depth of this RectBounds
     * The dimension along the Z-Axis, since this is a 2D bounds the return
     * value is always 0.0d.
     */
    @Override public double getDepth() {
        return 0.0d;
    }

    @Override public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    @Override public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    @Override public double getMinZ() {
        return 0.0d;
    }

    @Override public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    @Override public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    @Override public double getMaxZ() {
        return 0.0d;
    }

/*
    @Override public Vec2f getMin(Vec2f min) {
        if (min == null) {
            min = new Vec2f();
        }
        min.x = minX;
        min.y = minY;
        return min;
    }

    @Override public Vec2f getMax(Vec2f max) {
        if (max == null) {
            max = new Vec2f();
        }
        max.x = maxX;
        max.y = maxY;
        return max;
    }
*/

/*
    @Override public Vec3f getMin(Vec3f min) {
        if (min == null) {
            min = new Vec3f();
        }
        min.x = minX;
        min.y = minY;
        min.z = 0.0d;
        return min;

    }

    @Override public Vec3f getMax(Vec3f max) {
        if (max == null) {
            max = new Vec3f();
        }
        max.x = maxX;
        max.y = maxY;
        max.z = 0.0d;
        return max;

    }
*/

    @Override public BaseBounds deriveWithUnion(BaseBounds other) {
        if (other.getBoundsType() == BoundsType.RECTANGLE) {
            RectBounds rb = (RectBounds) other;
            unionWith(rb);
        } else if (other.getBoundsType() == BoundsType.BOX) {
            BoxBounds bb = new BoxBounds((BoxBounds) other);
            bb.unionWith(this);
            return bb;
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

/*
    @Override public BaseBounds deriveWithNewBounds(Rectangle other) {
        if (other.width < 0 || other.height < 0) return makeEmpty();
        setBounds(other.x, other.y,
                other.x + other.width, other.y + other.height);
        return this;
    }
*/

    @Override public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) return makeEmpty();
        if (other.getBoundsType() == BoundsType.RECTANGLE) {
            RectBounds rb = (RectBounds) other;
            minX = rb.getMinX();
            minY = rb.getMinY();
            maxX = rb.getMaxX();
            maxY = rb.getMaxY();
        } else if (other.getBoundsType() == BoundsType.BOX) {
            return new BoxBounds((BoxBounds) other);
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

    @Override public BaseBounds deriveWithNewBounds(double minX, double minY, double minZ,
                                                    double maxX, double maxY, double maxZ) {
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) return makeEmpty();
        if ((minZ == 0) && (maxZ == 0)) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            return this;
        }
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override public BaseBounds deriveWithNewBoundsAndSort(double minX, double minY, double minZ,
                                                           double maxX, double maxY, double maxZ) {
        if ((minZ == 0) && (maxZ == 0)) {
            setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
            return this;
        }

        BaseBounds bb = new BoxBounds();
        bb.setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return bb;
    }

    /**
     * Set the bounds to match that of the RectBounds object specified. The
     * specified bounds object must not be null.
     */
    public final void setBounds(RectBounds other) {
        minX = other.getMinX();
        minY = other.getMinY();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
    }

    /**
     * Set the bounds to the given values.
     */
    public final void setBounds(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Sets the bounds based on the given coords, and also ensures that after
     * having done so that this RectBounds instance is normalized.
     */
    public void setBoundsAndSort(double minX, double minY, double maxX, double maxY) {
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override public void setBoundsAndSort(double minX, double minY,  double minZ,
                                           double maxX, double maxY, double maxZ) {
        if (minZ != 0 || maxZ != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        setBounds(minX, minY, maxX, maxY);
        sortMinMax();
    }

    @Override public void setBoundsAndSort(Point2D p1, Point2D p2) {
        setBoundsAndSort(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    // Note: this implementation is exactly the same as BoxBounds. I could put a default
    // implementation in BaseBounds which calls the getters, or I could move the minX, minY
    // etc up to BaseBounds, or I could (maybe?) have BoxBounds extend from RectBounds or
    // have both extend a common parent. In the end I wanted direct access to the fields
    // but this was the only way to get it without making a more major change.
    @Override public RectBounds flattenInto(RectBounds bounds) {
        // Create the bounds if we need to
        if (bounds == null) bounds = new RectBounds();
        // Make it empty if we need to
        if (isEmpty()) return bounds.makeEmpty();
        // Populate it with values otherwise
        bounds.setBounds(minX, minY, maxX, maxY);
        return bounds;
    }

    public void unionWith(RectBounds other) {
        // Short circuit union if either bounds is empty.
        if (other.isEmpty()) return;
        if (this.isEmpty()) {
            setBounds(other);
            return;
        }

        minX = Math.min(minX, other.getMinX());
        minY = Math.min(minY, other.getMinY());
        maxX = Math.max(maxX, other.getMaxX());
        maxY = Math.max(maxY, other.getMaxY());
    }

    public void unionWith(double minX, double minY, double maxX, double maxY) {
        // Short circuit union if either bounds is empty.
        if ((maxX < minX) || (maxY < minY)) return;
        if (this.isEmpty()) {
            setBounds(minX, minY, maxX, maxY);
            return;
        }

        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
    }

    @Override public void add(double x, double y, double z) {
        if (z != 0) {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        unionWith(x, y, x, y);
    }

    public void add(double x, double y) {
        unionWith(x, y, x, y);
    }

    @Override public void add(Point2D p) {
        add(p.getX(), p.getY());
    }

    @Override public void intersectWith(BaseBounds other) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) return;
        if (other.isEmpty()) {
            makeEmpty();
            return;
        }

        minX = Math.max(minX, other.getMinX());
        minY = Math.max(minY, other.getMinY());
        maxX = Math.min(maxX, other.getMaxX());
        maxY = Math.min(maxY, other.getMaxY());
    }

/*
    @Override public void intersectWith(Rectangle other) {
        double x = other.x;
        double y = other.y;
        intersectWith(x, y, x + other.width, y + other.height);
    }
*/

    public void intersectWith(double minX, double minY, double maxX, double maxY) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) return;
        if ((maxX < minX) || (maxY < minY)) {
            makeEmpty();
            return;
        }

        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    @Override public void intersectWith(double minX, double minY, double minZ,
                                        double maxX, double maxY, double maxZ) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) return;
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) {
            makeEmpty();
            return;
        }

        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
    }

    @Override public boolean contains(Point2D p) {
        if ((p == null) || isEmpty()) return false;
        return (p.getX() >= minX && p.getX() <= maxX && p.getY() >= minY && p.getY() <= maxY);
    }

    @Override public boolean contains(double x, double y) {
        if (isEmpty()) return false;
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }

    /**
     * Determines whether the given <code>other</code> RectBounds is completely
     * contained within this RectBounds. Equivalent RectBounds will return true.
     *
     * @param other The other rect bounds to check against.
     * @return Whether the other rect bounds is contained within this one, which also
     * includes equivalence.
     */
    public boolean contains(RectBounds other) {
        if (isEmpty() || other.isEmpty()) return false;
        return minX <= other.minX && maxX >= other.maxX && minY <= other.minY && maxY >= other.maxY;
    }

    @Override public boolean intersects(double x, double y, double width, double height) {
        if (isEmpty()) return false;
        return (x + width >= minX &&
                y + height >= minY &&
                x <= maxX &&
                y <= maxY);
    }

    public boolean intersects(BaseBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return false;
        }
        return (other.getMaxX() >= minX &&
                other.getMaxY() >= minY &&
                other.getMaxZ() >= getMinZ() &&
                other.getMinX() <= maxX &&
                other.getMinY() <= maxY &&
                other.getMinZ() <= getMaxZ());
    }

    @Override public boolean disjoint(double x, double y, double width, double height) {
        if (isEmpty()) return true;
        return (x + width < minX ||
                y + height < minY ||
                x > maxX ||
                y > maxY);
    }

    public boolean disjoint(RectBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return true;
        }
        return (other.getMaxX() < minX ||
                other.getMaxY() < minY ||
                other.getMinX() > maxX ||
                other.getMinY() > maxY);
    }

    @Override public boolean isEmpty() {
        // NaN values will cause the comparisons to fail and return "empty"
        return !(maxX >= minX && maxY >= minY);
    }

    /**
     * Adjusts the edges of this RectBounds "outward" toward integral boundaries,
     * such that the rounded bounding box will always full enclose the original
     * bounding box.
     */
    @Override public void roundOut() {
        minX = Math.floor(minX);
        minY = Math.floor(minY);
        maxX = Math.ceil(maxX);
        maxY = Math.ceil(maxY);
    }

    public void grow(double h, double v) {
        minX -= h;
        maxX += h;
        minY -= v;
        maxY += v;
    }

    @Override public BaseBounds deriveWithPadding(double h, double v, double d) {
        if (d == 0) {
            grow(h, v);
            return this;
        }
        BoxBounds bb = new BoxBounds(minX, minY, 0, maxX, maxY, 0);
        bb.grow(h, v, d);
        return bb;
    }

    // for convenience, this function returns a reference to itself, so we can
    // change from using "bounds.makeEmpty(); return bounds;" to just
    // "return bounds.makeEmpty()"
    @Override public RectBounds makeEmpty() {
        minX = minY = 0.0d;
        maxX = maxY = -1.0d;
        return this;
    }

    @Override protected void sortMinMax() {
        if (minX > maxX) {
            double tmp = maxX;
            maxX = minX;
            minX = tmp;
        }
        if (minY > maxY) {
            double tmp = maxY;
            maxY = minY;
            minY = tmp;
        }
    }

    @Override public void translate(double x, double y, double z) {
        setMinX(getMinX() + x);
        setMinY(getMinY() + y);
        setMaxX(getMaxX() + x);
        setMaxY(getMaxY() + y);
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final RectBounds other = (RectBounds) obj;
        if (minX != other.getMinX()) return false;
        if (minY != other.getMinY()) return false;
        if (maxX != other.getMaxX()) return false;
        if (maxY != other.getMaxY()) return false;
        return true;
    }

    @Override public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Double.hashCode(minX);
        hash = 79 * hash + Double.hashCode(minY);
        hash = 79 * hash + Double.hashCode(maxX);
        hash = 79 * hash + Double.hashCode(maxY);
        return hash;
    }

    @Override public String toString() {
        return "RectBounds { minX:" + minX + ", minY:" + minY + ", maxX:" + maxX + ", maxY:" + maxY + "} (w:" + (maxX-minX) + ", h:" + (maxY-minY) +")";
    }
}
