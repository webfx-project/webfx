package webfx.demos.mandelbrot.mandelbrotmodel;

import javafx.scene.paint.Color;
import webfx.demos.mandelbrot.canvastracer.model.ModelPointValueToPixelColorConverter;

/**
 * @author Bruno Salmon
 */
public class MandelbrotPointValueToPixelColorConverter implements ModelPointValueToPixelColorConverter {

    @Override
    public Color convertModelPointValueToPixelColor(Object mpv) {
        MandelbrotPointValue mbpv = (MandelbrotPointValue) mpv;
        MandelbrotModel model = mbpv.model;
        if (model.paletteColors == null) {
            int offset = model.paletteMapping.getOffset();
            int length = model.paletteMapping.getLength();
            if (length == 0)
                length = model.maxIterations;
            model.paletteColors = model.palette.makeRGBs(length,offset);
        }

        Color color;
        if (mbpv.count == model.maxIterations)
            color = model.mandelbrotColor;
        else
            color = model.paletteColors[mbpv.count % model.paletteColors.length];

        return color;
    }
}
