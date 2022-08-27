package javafx.scene.image;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public class CanvasPixelWriter extends AbstractPixelWriter {

    private static int idSeq;
    private final Canvas canvas;
    private final GraphicsContext gc;

    public CanvasPixelWriter(Image image) {
        super(image);
        canvas = new Canvas(image.getWidth(), image.getHeight());
        gc = canvas.getGraphicsContext2D();
        image.setPeerCanvas(canvas.getOrCreateAndBindNodePeer());
        image.setPeerCanvasDirty(true);
    }

    public CanvasPixelWriter(double width, double height) {
        this(new Canvas(width, height));
    }
    public CanvasPixelWriter(Canvas canvas) {
        this.canvas = canvas;
        canvas.setId("canvas-" + ++idSeq);
        gc = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setColor(int x, int y, Color c) { // The only implemented method for now
        if (Color.TRANSPARENT.equals(c))
            gc.clearRect(x, y, 1, 1);
        else {
            gc.setFill(c);
            gc.fillRect(x, y, 1, 1);
        }
    }

}
