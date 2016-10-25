package naga.providers.toolkit.javafx.util;

import naga.toolkit.drawing.shapes.StrokeLineCap;

/**
 * @author Bruno Salmon
 */
public class FxStrokes {

    public static javafx.scene.shape.StrokeLineCap toFxStrokeLineCap(StrokeLineCap strokeLineCap) {
        switch (strokeLineCap) {
            case BUTT: return javafx.scene.shape.StrokeLineCap.BUTT;
            case ROUND: return javafx.scene.shape.StrokeLineCap.ROUND;
            case SQUARE: return javafx.scene.shape.StrokeLineCap.SQUARE;
        }
        return null;
    }
}
