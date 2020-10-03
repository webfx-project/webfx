package webfx.demos.mandelbrot.canvastracer.model;

import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public interface ModelPointValueToPixelColorConverter {

    Color convertModelPointValueToPixelColor(Object mpv);

}
