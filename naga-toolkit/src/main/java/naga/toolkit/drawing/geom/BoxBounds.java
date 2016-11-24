package naga.toolkit.drawing.geom;

import naga.toolkit.drawing.shapes.Point2D;

/**
 *
 */
public class BoxBounds extends BaseBounds {
    // minimum x value of boundining box
    private double minX;
    // maximum x value of boundining box
    private double maxX;
    // minimum y value of boundining box
    private double minY;
    // maximum y value of boundining box
    private double maxY;
    // minimum z value of boundining box
    private double minZ;
    // maximum z value of boundining box
    private double maxZ;

    /**
     * Create an axis aligned bounding box object, with an empty bounds
     * where maxX < minX, maxY < minY and maxZ < minZ.
     */
    public BoxBounds() {
        minX = minY = minZ = 0.0d;
        maxX = maxY = maxZ = -1.0d;
    }

    public BaseBounds copy() {
        return new BoxBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates an axis aligned bounding box based on the minX, minY, minZ, maxX, maxY,
     * and maxZ values specified.
     */
    public BoxBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates an axis aligned bounding box as a copy of the specified
     * BoxBounds object.
     */
    public BoxBounds(BoxBounds other) {
        setBounds(other);
    }

    public BoundsType getBoundsType() {
        return BoundsType.BOX;
    }

    public boolean is2D() {
        return false;
    }

    /**
     * Convenience function for getting the width of this bounds.
     * The dimension along the X-Axis.
     */
    public double getWidth() {
        return maxX - minX;
    }

    /**
     * Convenience function for getting the height of this bounds.
     * The dimension along the Y-Axis.
     */
    public double getHeight() {
        return maxY - minY;
    }

    /**
     * Convenience function for getting the depth of this bounds.
     * The dimension along the Z-Axis.
     */
    public double getDepth() {
        return maxZ - minZ;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }

/*
    public Vec2f getMin(Vec2f min) {
        if (min == null) {
            min = new Vec2f();
        }
        min.x = minX;
        min.y = minY;
        return min;
    }

    public Vec2f getMax(Vec2f max) {
        if (max == null) {
            max = new Vec2f();
        }
        max.x = maxX;
        max.y = maxY;
        return max;
    }
*/

/*    public Vec3f getMin(Vec3f min) {
        if (min == null) {
            min = new Vec3f();
        }
        min.x = minX;
        min.y = minY;
        min.z = minZ;
        return min;

    }

    public Vec3f getMax(Vec3f max) {
        if (max == null) {
            max = new Vec3f();
        }
        max.x = maxX;
        max.y = maxY;
        max.z = maxZ;
        return max;

    }*/

    public BaseBounds deriveWithUnion(BaseBounds other) {
        if ((other.getBoundsType() == BoundsType.RECTANGLE) ||
                (other.getBoundsType() == BoundsType.BOX)) {
            unionWith(other);
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

/*
    public BaseBounds deriveWithNewBounds(Rectangle other) {
        if (other.width < 0 || other.height < 0) return makeEmpty();
        setBounds(other.x, other.y, 0,
                other.x + other.width, other.y + other.height, 0);
        return this;
    }
*/

    public BaseBounds deriveWithNewBounds(BaseBounds other) {
        if (other.isEmpty()) return makeEmpty();
        if ((other.getBoundsType() == BoundsType.RECTANGLE) ||
                (other.getBoundsType() == BoundsType.BOX)) {
            minX = other.getMinX();
            minY = other.getMinY();
            minZ = other.getMinZ();
            maxX = other.getMaxX();
            maxY = other.getMaxY();
            maxZ = other.getMaxZ();
        } else {
            throw new UnsupportedOperationException("Unknown BoundsType");
        }
        return this;
    }

    public BaseBounds deriveWithNewBounds(double minX, double minY, double minZ,
                                          double maxX, double maxY, double maxZ) {
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) return makeEmpty();
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        return this;
    }

    public BaseBounds deriveWithNewBoundsAndSort(double minX, double minY, double minZ,
                                                 double maxX, double maxY, double maxZ) {
        setBoundsAndSort(minX, minY, minZ, maxX, maxY, maxZ);
        return this;
    }

    @Override public RectBounds flattenInto(RectBounds bounds) {
        // Create the bounds if we need to
        if (bounds == null) bounds = new RectBounds();
        // Make it empty if we need to
        if (isEmpty()) return bounds.makeEmpty();
        // Populate it with values otherwise
        bounds.setBounds(minX, minY, maxX, maxY);
        return bounds;
    }

    /**
     * Set the bounds to match that of the BaseBounds object specified. The
     * specified bounds object must not be null.
     */
    public final void setBounds(BaseBounds other) {
        minX = other.getMinX();
        minY = other.getMinY();
        minZ = other.getMinZ();
        maxX = other.getMaxX();
        maxY = other.getMaxY();
        maxZ = other.getMaxZ();
    }

    /**
     * Set the bounds to the given values.
     */
    public final void setBounds(double minX, double minY,  double minZ,
                                double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public void setBoundsAndSort(double minX, double minY,  double minZ,
                                 double maxX, double maxY, double maxZ) {
        setBounds(minX, minY, minZ, maxX, maxY, maxZ);
        sortMinMax();
    }

    public void setBoundsAndSort(Point2D p1, Point2D p2) {
        setBoundsAndSort(p1.getX(), p1.getY(), 0.0d, p2.getX(), p2.getY(), 0.0d);
    }

    public void unionWith(BaseBounds other) {
        // Short circuit union if either bounds is empty.
        if (other.isEmpty()) return;
        if (this.isEmpty()) {
            setBounds(other);
            return;
        }

        minX = Math.min(minX, other.getMinX());
        minY = Math.min(minY, other.getMinY());
        minZ = Math.min(minZ, other.getMinZ());
        maxX = Math.max(maxX, other.getMaxX());
        maxY = Math.max(maxY, other.getMaxY());
        maxZ = Math.max(maxZ, other.getMaxZ());
    }


    public void unionWith(double minX, double minY, double minZ,
                          double maxX, double maxY, double maxZ) {
        // Short circuit union if either bounds is empty.
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) return;
        if (this.isEmpty()) {
            setBounds(minX, minY, minZ, maxX, maxY, maxZ);
            return;
        }

        this.minX = Math.min(this.minX, minX);
        this.minY = Math.min(this.minY, minY);
        this.minZ = Math.min(this.minZ, minZ);
        this.maxX = Math.max(this.maxX, maxX);
        this.maxY = Math.max(this.maxY, maxY);
        this.maxZ = Math.max(this.maxZ, maxZ);
    }

    public void add(double x, double y, double z) {
        unionWith(x, y, z, x, y, z);
    }

    public void add(Point2D p) {
        add(p.getX(), p.getY(), 0.0d);
    }

/*
    public void intersectWith(Rectangle other) {
        double x = other.x;
        double y = other.y;
        intersectWith(x, y, 0,
                x + other.width, y + other.height, 0);
    }
*/

    public void intersectWith(BaseBounds other) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) return;
        if (other.isEmpty()) {
            makeEmpty();
            return;
        }

        minX = Math.max(minX, other.getMinX());
        minY = Math.max(minY, other.getMinY());
        minZ = Math.max(minZ, other.getMinZ());
        maxX = Math.min(maxX, other.getMaxX());
        maxY = Math.min(maxY, other.getMaxY());
        maxZ = Math.min(maxZ, other.getMaxZ());
    }

    public void intersectWith(double minX, double minY, double minZ,
                              double maxX, double maxY, double maxZ) {
        // Short circuit intersect if either bounds is empty.
        if (this.isEmpty()) return;
        if ((maxX < minX) || (maxY < minY) || (maxZ < minZ)) {
            makeEmpty();
            return;
        }

        this.minX = Math.max(this.minX, minX);
        this.minY = Math.max(this.minY, minY);
        this.minZ = Math.max(this.minZ, minZ);
        this.maxX = Math.min(this.maxX, maxX);
        this.maxY = Math.min(this.maxY, maxY);
        this.maxZ = Math.min(this.maxZ, maxZ);
    }

    public boolean contains(Point2D p) {
        if ((p == null) || isEmpty()) return false;
        return contains(p.getX(), p.getY(), 0.0d);
    }

    public boolean contains(double x, double y) {
        if (isEmpty()) return false;
        return contains(x, y, 0.0d);
    }

    public boolean contains(double x, double y, double z) {
        if (isEmpty()) return false;
        return (x >= minX && x <= maxX && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ);
    }

    public boolean contains(double x, double y, double z,
                            double width, double height, double depth) {
        if (isEmpty()) return false;
        return contains(x, y, z) && contains(x+width, y+height, z+depth);
    }

    public boolean intersects(double x, double y, double width, double height) {
        return intersects(x, y, 0.0d, width, height, 0.0d);
    }

    public boolean intersects(double x, double y, double z,
                              double width, double height, double depth) {
        if (isEmpty()) return false;
        return (x + width >= minX &&
                y + height >= minY &&
                z + depth >= minZ &&
                x <= maxX &&
                y <= maxY &&
                z <= maxZ);
    }

    public boolean intersects(BaseBounds other) {
        if ((other == null) || other.isEmpty() || isEmpty()) {
            return false;
        }
        return (other.getMaxX() >= minX &&
                other.getMaxY() >= minY &&
                other.getMaxZ() >= minZ &&
                other.getMinX() <= maxX &&
                other.getMinY() <= maxY &&
                other.getMinZ() <= maxZ);
    }

    public boolean disjoint(double x, double y, double width, double height) {
        return disjoint(x, y, 0d, width, height, 0d);
    }

    public boolean disjoint(double x, double y, double z,
                            double width, double height, double depth) {
        if (isEmpty()) return true;
        return (x + width < minX ||
                y + height < minY ||
                z + depth < minZ ||
                x > maxX ||
                y > maxY ||
                z > maxZ);
    }

    public boolean isEmpty() {
        return maxX < minX || maxY < minY || maxZ < minZ;
    }

    /**
     * Adjusts the edges of this BoxBounds "outward" toward integral boundaries,
     * such that the rounded bounding box will always full enclose the original
     * bounding box.
     */
    public void roundOut() {
        minX = Math.floor(minX);
        minY = Math.floor(minY);
        minZ = Math.floor(minZ);
        maxX = Math.ceil(maxX);
        maxY = Math.ceil(maxY);
        maxZ = Math.ceil(maxZ);
    }

    public void grow(double h, double v, double d) {
        minX -= h;
        maxX += h;
        minY -= v;
        maxY += v;
        minZ -= d;
        maxZ += d;
    }

    public BaseBounds deriveWithPadding(double h, double v, double d) {
        grow(h, v, d);
        return this;
    }

    // for convenience, this function returns a reference to itself, so we can
    // change from using "bounds.makeEmpty(); return bounds;" to just
    // "return bounds.makeEmpty()"
    public BoxBounds makeEmpty() {
        minX = minY = minZ = 0.0d;
        maxX = maxY = maxZ = -1.0d;
        return this;
    }

    protected void sortMinMax() {
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
        if (minZ > maxZ) {
            double tmp = maxZ;
            maxZ = minZ;
            minZ = tmp;
        }
    }

    @Override
    public void translate(double x, double y, double z) {
        setMinX(getMinX() + x);
        setMinY(getMinY() + y);
        setMaxX(getMaxX() + x);
        setMaxY(getMaxY() + y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        final BoxBounds other = (BoxBounds) obj;
        if (minX != other.getMinX()) return false;
        if (minY != other.getMinY()) return false;
        if (minZ != other.getMinZ()) return false;
        if (maxX != other.getMaxX()) return false;
        if (maxY != other.getMaxY()) return false;
        if (maxZ != other.getMaxZ()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Double.hashCode(minX);
        hash = 79 * hash + Double.hashCode(minY);
        hash = 79 * hash + Double.hashCode(minZ);
        hash = 79 * hash + Double.hashCode(maxX);
        hash = 79 * hash + Double.hashCode(maxY);
        hash = 79 * hash + Double.hashCode(maxZ);

        return hash;
    }

    @Override
    public String toString() {
        return "BoxBounds { minX:" + minX + ", minY:" + minY + ", minZ:" + minZ + ", maxX:" + maxX + ", maxY:" + maxY + ", maxZ:" + maxZ + "}";
    }

}
