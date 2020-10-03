package webfx.demos.mandelbrot.canvastracer;

import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public class RandomPixelColorComputer implements PixelColorComputer {

    @Override
    public Color computePixelColor(CanvasPixel cp) {
        return Color.color(Math.random(), Math.random(), Math.random());
    }
}
