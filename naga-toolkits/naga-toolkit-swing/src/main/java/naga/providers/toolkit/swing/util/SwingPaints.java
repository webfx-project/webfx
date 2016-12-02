package naga.providers.toolkit.swing.util;


import naga.commons.util.collection.Collections;
import naga.toolkit.fx.scene.paint.Color;
import naga.toolkit.fx.scene.paint.*;
import naga.toolkit.fx.scene.paint.Paint;

import java.awt.*;
import java.util.stream.Collectors;


/**
 * @author Bruno Salmon
 */
public class SwingPaints {

    public static java.awt.Paint toSwingPaint(Paint paint) {
        if (paint instanceof Color)
            return toSwingColor((Color) paint);
        if (paint instanceof LinearGradient)
            return toSwingLinearGradient((LinearGradient) paint);
        return null;
    }

    public static java.awt.Color toSwingColor(Color color) {
        return new java.awt.Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }

    public static java.awt.LinearGradientPaint toSwingLinearGradient(LinearGradient lg) {
        return toSwingLinearGradient(lg, 1, 1);
    }

    public static java.awt.LinearGradientPaint toSwingLinearGradient(LinearGradient lg, float width, float height) {
        float[] swingFractions = Collections.toFloatArray(lg.getStops().stream().map(stop -> (float) stop.getOffset()).collect(Collectors.toList()));
        java.awt.Color[] swingColors = lg.getStops().stream().map(stop -> toSwingColor(stop.getColor())).toArray(java.awt.Color[]::new);
        CycleMethod cycleMethod = lg.getCycleMethod();
        MultipleGradientPaint.CycleMethod swingCycleMethod = cycleMethod == CycleMethod.REPEAT ? MultipleGradientPaint.CycleMethod.REPEAT : cycleMethod == CycleMethod.REFLECT ? MultipleGradientPaint.CycleMethod.REFLECT : MultipleGradientPaint.CycleMethod.NO_CYCLE;
        float startX = (float) lg.getStartX();
        float startY = (float) lg.getStartY();
        float endX = (float) lg.getEndX();
        float endY = (float) lg.getEndY();
        if (lg.isProportional()) {
            startX *= width;
            endX *= width;
            startY *= height;
            endY *= height;
        }
        return new LinearGradientPaint(startX, startY, endX, endY, swingFractions, swingColors, swingCycleMethod);
    }

}
