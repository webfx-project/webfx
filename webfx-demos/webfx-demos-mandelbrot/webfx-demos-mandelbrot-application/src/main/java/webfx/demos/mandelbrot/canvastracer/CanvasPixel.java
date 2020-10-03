package webfx.demos.mandelbrot.canvastracer;

/**
 * @author Bruno Salmon
 */
public class CanvasPixel {

    public double x;
    public double y;
    public double canvasWidth;
    public double canvasHeight;

    public void set(double x, double y, double canvasWidth, double canvasHeight) {
        this.x = x;
        this.y = y;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }
}
