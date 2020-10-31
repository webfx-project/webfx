package webfx.demo.mandelbrot.tracerframework;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import static webfx.demo.mandelbrot.tracerframework.TracerView.animateProperty;

/**
 * @author Bruno Salmon
 */
public final class TracerThumbnail extends StackPane {

    private final static double INITIAL_SCALE = 0.9;
    private final Canvas canvas = new Canvas();
    private TracerEngine thumbnailTracer;

    public TracerThumbnail() {
        getChildren().setAll(canvas);
        StackPane.setAlignment(canvas, Pos.CENTER);
        setMinSize(0, 0);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setThumbnailTracer(TracerEngine thumbnailTracer) {
        this.thumbnailTracer = thumbnailTracer;
    }

    public TracerEngine getThumbnailTracer() {
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
        canvas.setWidth(width / INITIAL_SCALE);
        canvas.setHeight(height / INITIAL_SCALE);
        if (thumbnailTracer != null) {
            // Only 1 worker per thumbnail (we have 9 thumbnails) otherwise it may be too many workers for the device
            // (ex: 9 * 8 cores = 72 workers make the page crash on my mobile)
            thumbnailTracer.setThreadsCount(1);
            thumbnailTracer.start();
        }
        // Zoom effect when hovering the thumbnails
        DoubleProperty scaleProperty = new SimpleDoubleProperty() {
            @Override
            protected void invalidated() {
                double scale = get();
                setScaleX(scale);
                setScaleY((scale));
                setClip(new Rectangle(width * (1 - 1 / scale) / 2, height * (1 - 1 / scale) / 2, width / scale, height / scale));
            }
        };
        scaleProperty.setValue(INITIAL_SCALE);
        setOnMouseEntered(e -> animateProperty(200, scaleProperty, 1.0));
        setOnMouseExited( e -> animateProperty(200, scaleProperty, 0.9));
    }
}
