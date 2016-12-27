package naga.providers.toolkit.javafx.util;

import naga.toolkit.fx.scene.shape.StrokeLineCap;
import naga.toolkit.fx.scene.shape.StrokeLineJoin;
import naga.toolkit.fx.scene.shape.StrokeType;

/**
 * @author Bruno Salmon
 */
public class FxStrokes {

    public static javafx.scene.shape.StrokeType toFxStrokeType(StrokeType strokeType) {
        if (strokeType != null)
            switch (strokeType) {
                case CENTERED: return javafx.scene.shape.StrokeType.CENTERED;
                case INSIDE: return javafx.scene.shape.StrokeType.INSIDE;
                case OUTSIDE: return javafx.scene.shape.StrokeType.OUTSIDE;
            }
        return null;
    }

    public static javafx.scene.shape.StrokeLineCap toFxStrokeLineCap(StrokeLineCap strokeLineCap) {
        if (strokeLineCap != null)
            switch (strokeLineCap) {
                case BUTT: return javafx.scene.shape.StrokeLineCap.BUTT;
                case ROUND: return javafx.scene.shape.StrokeLineCap.ROUND;
                case SQUARE: return javafx.scene.shape.StrokeLineCap.SQUARE;
            }
        return null;
    }

    public static javafx.scene.shape.StrokeLineJoin toFxStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        if (strokeLineJoin != null)
            switch (strokeLineJoin) {
                case MITER: return javafx.scene.shape.StrokeLineJoin.MITER;
                case BEVEL: return javafx.scene.shape.StrokeLineJoin.BEVEL;
                case ROUND: return javafx.scene.shape.StrokeLineJoin.ROUND;
            }
        return null;
    }
}
