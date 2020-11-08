package webfx.demo.enzoclocks.circlespacker;

/**
 * @author Bruno Salmon
 */
abstract class CirclesPackerBase implements CirclesPacker {

    protected double width, height, radius;
    protected boolean horizontal;
    protected int count;
    protected double w, h;
    private int lastCenterComputationIndex;
    protected double cx, cy;

    @Override
    public void setContainerSize(double width, double height) {
        if (width != this.width || height != this.height) {
            this.width = width;
            this.height = height;
            markAsChanged();
        }
    }

    @Override
    public void setCirclesCount(int count) {
        if (count != this.count) {
            this.count = count;
            markAsChanged();
        }
    }

    private void markAsChanged() {
        radius = 0;
        lastCenterComputationIndex = -1;
    }

    @Override
    public boolean hasChanged() {
        return radius == 0;
    }

    @Override
    public double getCirclesRadius() {
        if (radius == 0 && count > 0) {
            initFields();
            computeCirclesRadius();
        }
        return radius;
    }

    protected void initFields() {
        horizontal = width >= height;
        w = horizontal ? width : height;
        h = horizontal ? height : width;
    }

    protected abstract void computeCirclesRadius();

    @Override
    public double getCircleCenterX(int index) {
        computeCircleCenterCoordinatesIfIndexChanged(index);
        return cx;
    }

    @Override
    public double getCircleCenterY(int index) {
        computeCircleCenterCoordinatesIfIndexChanged(index);
        return cy;
    }

    private void computeCircleCenterCoordinatesIfIndexChanged(int index) {
        if (index != lastCenterComputationIndex)
            computeCircleCenterCoordinates(lastCenterComputationIndex = index);
    }

    protected abstract void computeCircleCenterCoordinates(int index);

}
