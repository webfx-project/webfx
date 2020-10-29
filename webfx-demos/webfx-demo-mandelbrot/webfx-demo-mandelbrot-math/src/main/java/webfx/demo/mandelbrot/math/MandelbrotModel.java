/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package webfx.demo.mandelbrot.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class MandelbrotModel extends MandelbrotViewport {

    int width, height, placeIndex, frameIndex;

    private static final int HP_CUTOFF_EXP = 15;
    private static final RoundingMode RM = RoundingMode.HALF_EVEN;
    private static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal TEN = new BigDecimal("10");

    private final static double COMPUTING_ZOOM_FACTOR = 0.9;

    void moveToNextFrame() {
        zoom(COMPUTING_ZOOM_FACTOR);
    }

    void adjustAspect(double canvasWidth, double canvasHeight) {  // adjust requested x/y limits to match aspect ration of image
        width = (int) canvasWidth;
        height = (int) canvasHeight;
        if (xmin.scale() < HP_CUTOFF_EXP + 8)
            xmin = xmin.setScale(HP_CUTOFF_EXP + 8, RM);
        if (xmax.scale() < HP_CUTOFF_EXP + 8)
            xmax = xmax.setScale(HP_CUTOFF_EXP + 8, RM);
        if (ymin.scale() < HP_CUTOFF_EXP + 8)
            ymin = ymin.setScale(HP_CUTOFF_EXP + 8, RM);
        if (ymax.scale() < HP_CUTOFF_EXP + 8)
            ymax = ymax.setScale(HP_CUTOFF_EXP + 8, RM);
        BigDecimal dx = xmax.subtract(xmin).setScale(Math.max(xmax.scale(), HP_CUTOFF_EXP) * 2, RM);
        dx = dx.divide(new BigDecimal(canvasWidth), RM);
        int precision = 0;
        while (dx.compareTo(TWO) < 0) {
            precision++;
            dx = dx.multiply(TEN);
        }
        if (precision < HP_CUTOFF_EXP)
            precision = HP_CUTOFF_EXP;
        int scale = precision + 5 + (precision - 10) / 10;
        xmin = xmin.setScale(scale, RM);
        xmax = xmax.setScale(scale, RM);
        ymin = ymin.setScale(scale, RM);
        ymax = ymax.setScale(scale, RM);

        BigDecimal width = xmax.subtract(xmin);
        BigDecimal height = ymax.subtract(ymin);
        BigDecimal aspect = width.divide(height, RM);
        BigDecimal windowAspect = new BigDecimal(canvasWidth/canvasHeight);
        if (aspect.compareTo(windowAspect) < 0) {
            BigDecimal newWidth = width.multiply(windowAspect).divide(aspect, RM);
            BigDecimal center = xmax.add(xmin).divide(TWO, RM);
            xmax = center.add(newWidth.divide(TWO, RM)).setScale(scale, RM);
            xmin = center.subtract(newWidth.divide(TWO, RM)).setScale(scale, RM);
        }
        else if (aspect.compareTo(windowAspect) > 0) {
            BigDecimal newHeight = height.multiply(aspect).divide(windowAspect, RM);
            BigDecimal center = ymax.add(ymin).divide(TWO, RM);
            ymax = center.add(newHeight.divide(TWO, RM)).setScale(scale, RM);
            ymin = center.subtract(newHeight.divide(TWO, RM)).setScale(scale, RM);
        }
    }

    void zoom(double zoomFactor) {
        BigDecimal xc = xmin.add(xmax).divide(TWO, RM);
        BigDecimal yc = ymin.add(ymax).divide(TWO, RM);
        BigDecimal halfWidth = BigDecimal.valueOf(xmax.subtract(xmin).doubleValue() / 2 * zoomFactor);
        BigDecimal halfHeight = BigDecimal.valueOf(ymax.subtract(ymin).doubleValue() / 2 * zoomFactor);
        xmin = xc.subtract(halfWidth);
        xmax = xc.add(halfWidth);
        ymin = yc.subtract(halfHeight);
        ymax = yc.add(halfHeight);
    }

    static MandelbrotModel ofViewport(MandelbrotViewport viewport) {
        MandelbrotModel model = new MandelbrotModel();
        model.xmin = BigDecimal.valueOf(viewport.xmin.doubleValue());
        model.xmax = BigDecimal.valueOf(viewport.xmax.doubleValue());
        model.ymin = BigDecimal.valueOf(viewport.ymin.doubleValue());
        model.ymax = BigDecimal.valueOf(viewport.ymax.doubleValue());
        model.maxIterations = viewport.maxIterations;
        return model;
    }
}
