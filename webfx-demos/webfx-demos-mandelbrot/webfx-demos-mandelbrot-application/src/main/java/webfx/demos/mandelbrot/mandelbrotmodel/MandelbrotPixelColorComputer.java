package webfx.demos.mandelbrot.mandelbrotmodel;

import webfx.demos.mandelbrot.canvastracer.model.ModelPixelColorComputer;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotPixelColorComputer {

    public static ModelPixelColorComputer create(MandelbrotModel model, double canvasWidth, double canvasHeight) {
        model.adjustAspect(canvasWidth, canvasHeight);
        return new ModelPixelColorComputer(
                new CanvasPixelToMandelbrotPointConverter(model),
                new MandelbrotPointValueComputer(),
                new MandelbrotPointValueToPixelColorConverter()
        );
    }

}
