package webfx.demos.mandelbrot.canvastracer.model;

import webfx.demos.mandelbrot.canvastracer.CanvasPixel;

/**
 * @author Bruno Salmon
 */
public interface CanvasPixelToModelPointConverter {

    Object convertCanvasPixelToModelPoint(CanvasPixel cp);

}
