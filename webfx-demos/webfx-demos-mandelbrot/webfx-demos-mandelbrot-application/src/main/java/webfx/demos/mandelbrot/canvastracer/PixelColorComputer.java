package webfx.demos.mandelbrot.canvastracer;

import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public interface PixelColorComputer {

    Color computePixelColor(CanvasPixel canvasPixel);

}
