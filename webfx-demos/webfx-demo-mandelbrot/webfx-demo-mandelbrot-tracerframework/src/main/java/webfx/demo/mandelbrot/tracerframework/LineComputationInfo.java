package webfx.demo.mandelbrot.tracerframework;

/**
 * @author Bruno Salmon
 */
public class LineComputationInfo {
    public int cy; // vertical position of the line in the canvas
    public int cx; // used only in mono-thread (ex: browser) to memorize the horizontal position where the computation stopped and the end of the animation frame
    public Object linePixelResultStorage;
}
