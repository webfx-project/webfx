package webfx.demos.mandelbrot.canvastracer.model;

import javafx.scene.paint.Color;
import webfx.demos.mandelbrot.canvastracer.PixelColorComputer;
import webfx.demos.mandelbrot.canvastracer.CanvasPixel;

/**
 * @author Bruno Salmon
 */
public class ModelPixelColorComputer implements PixelColorComputer {

    private CanvasPixelToModelPointConverter canvasPixelToModelPointConverter;
    private ModelPointValueComputer modelPointValueComputer;
    private ModelPointValueToPixelColorConverter modelPointValueToPixelColorConverter;

    public ModelPixelColorComputer() {
    }

    public ModelPixelColorComputer(CanvasPixelToModelPointConverter canvasPixelToModelPointConverter, ModelPointValueComputer modelPointValueComputer, ModelPointValueToPixelColorConverter modelPointValueToPixelColorConverter) {
        this.canvasPixelToModelPointConverter = canvasPixelToModelPointConverter;
        this.modelPointValueComputer = modelPointValueComputer;
        this.modelPointValueToPixelColorConverter = modelPointValueToPixelColorConverter;
    }

    @Override
    public Color computePixelColor(CanvasPixel cp) {
        Object mp = canvasPixelToModelPointConverter.convertCanvasPixelToModelPoint(cp);
        Object mpv = modelPointValueComputer.computeModelPointValue(mp);
        Color pc = modelPointValueToPixelColorConverter.convertModelPointValueToPixelColor(mpv);
        return pc;
    }

    public void setCanvasPixelToModelPointConverter(CanvasPixelToModelPointConverter canvasPixelToModelPointConverter) {
        this.canvasPixelToModelPointConverter = canvasPixelToModelPointConverter;
    }

    public void setModelPointValueComputer(ModelPointValueComputer modelPointValueComputer) {
        this.modelPointValueComputer = modelPointValueComputer;
    }

    public void setModelPointValueToPixelColorConverter(ModelPointValueToPixelColorConverter modelPointValueToPixelColorConverter) {
        this.modelPointValueToPixelColorConverter = modelPointValueToPixelColorConverter;
    }
}
