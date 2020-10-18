package webfx.demos.mandelbrot;

import javafx.scene.canvas.Canvas;

/**
 * @author Bruno Salmon
 */
final class ThumbnailCanvas extends Canvas {

    private MandelbrotTracer thumbnailTracer;

    public void setThumbnailTracer(MandelbrotTracer thumbnailTracer) {
        this.thumbnailTracer = thumbnailTracer;
    }

    public MandelbrotTracer getThumbnailTracer() {
        return thumbnailTracer;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double width, double height) {
        if (thumbnailTracer != null)
            thumbnailTracer.stop();
        setWidth(width);
        setHeight(height);
        thumbnailTracer.getModel().adjustAspect(width, height);
        if (thumbnailTracer != null) {
            thumbnailTracer.setThreadsCount(1); // Only 1 worker per thumbnail otherwise it may be too much for the device
            thumbnailTracer.start();
        }
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double maxWidth(double height) {
        return Double.MAX_VALUE;
    }

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    public double maxHeight(double width) {
        return Double.MAX_VALUE;
    }
}
