package naga.providers.toolkit.javafx.util;


import naga.toolkit.fx.scene.paint.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public class FxPaints {

    public static javafx.scene.paint.Paint toFxPaint(Paint paint) {
        if (paint instanceof Color)
            return toFxColor((Color) paint);
        if (paint instanceof LinearGradient)
            return toFxLinearGradient((LinearGradient) paint);
        return null;
    }

    public static javafx.scene.paint.Color toFxColor(Color color) {
        return new javafx.scene.paint.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

    public static javafx.scene.paint.LinearGradient toFxLinearGradient(LinearGradient lg) {
        return new javafx.scene.paint.LinearGradient(lg.getStartX(), lg.getStartY(), lg.getEndX(), lg.getEndY(), lg.isProportional(), toFxCycleMethod(lg.getCycleMethod()), toFxStops(lg.getStops()));
    }

    private static javafx.scene.paint.CycleMethod toFxCycleMethod(CycleMethod cycleMethod) {
        switch (cycleMethod) {
            case NO_CYCLE: return javafx.scene.paint.CycleMethod.NO_CYCLE;
            case REFLECT: return javafx.scene.paint.CycleMethod.REFLECT;
            case REPEAT: return javafx.scene.paint.CycleMethod.REPEAT;
        }
        return null;
    }

    private static List<javafx.scene.paint.Stop> toFxStops(List<Stop> stops) {
        return stops.stream().map(stop -> new javafx.scene.paint.Stop(stop.getOffset(), toFxColor(stop.getColor()))).collect(Collectors.toList());
    }

}
