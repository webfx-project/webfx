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

package webfx.demos.mandelbrot;

import javafx.scene.paint.Color;
import webfx.demos.mandelbrot.computation.MandelbrotViewport;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MandelbrotModel extends MandelbrotViewport {

    int maxIterations;
    boolean highPrecision;

    Palette palette;
    PaletteMapping paletteMapping;

    public String thumbnailUrl;
    int thumbnailFrame;
    private ThumbnailCanvas thumbnailCanvas;

    // Generated data
    Color mandelbrotColor;
    Color[] paletteColors;

    private static final int HP_CUTOFF_EXP = 15;
    private static final RoundingMode RM = RoundingMode.HALF_EVEN;
    public static final BigDecimal TWO = new BigDecimal("2");
    private static final BigDecimal TEN = new BigDecimal("10");

    public MandelbrotModel() {
    }

    public ThumbnailCanvas getThumbnailCanvas() {
        if (thumbnailCanvas == null) {
            thumbnailCanvas = new ThumbnailCanvas();
            MandelbrotTracer thumbnailTracer = new MandelbrotTracer(thumbnailCanvas);
            MandelbrotModel model = duplicate();
            for (int i = 1; i <= thumbnailFrame; i++)
                model.zoom(MandelbrotApplication.COMPUTING_ZOOM_FACTOR);
            thumbnailTracer.setModel(model);
            thumbnailCanvas.setThumbnailTracer(thumbnailTracer);
        }
        return thumbnailCanvas;
    }

    public void adjustAspect(double canvasWidth, double canvasHeight) {  // adjust requested x/y limits to match aspect ration of image
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

    public void translatePercent(double percentX, double percentY) {
        BigDecimal width = xmax.subtract(xmin);
        BigDecimal height = ymax.subtract(ymin);
        translate(percentX * width.doubleValue(), percentY * height.doubleValue());
    }

    public void translate(double dX, double dY) {
        xmin = xmin.add(BigDecimal.valueOf(dX));
        xmax = xmax.add(BigDecimal.valueOf(dX));
        ymin = ymin.add(BigDecimal.valueOf(dY));
        ymax = ymax.add(BigDecimal.valueOf(dY));
    }

    public void zoom(double zoomFactor) {
        BigDecimal xc = xmin.add(xmax).divide(TWO, RM);
        BigDecimal yc = ymin.add(ymax).divide(TWO, RM);
        BigDecimal halfWidth = BigDecimal.valueOf(xmax.subtract(xmin).doubleValue() / 2 * zoomFactor);
        BigDecimal halfHeight = BigDecimal.valueOf(ymax.subtract(ymin).doubleValue() / 2 * zoomFactor);
        xmin = xc.subtract(halfWidth);
        xmax = xc.add(halfWidth);
        ymin = yc.subtract(halfHeight);
        ymax = yc.add(halfHeight);
    }

    public void centerPercent(double percentX, double percentY) {
        BigDecimal width = xmax.subtract(xmin);
        BigDecimal height = ymax.subtract(ymin);
        center(xmin.doubleValue() + percentX * width.doubleValue(),ymin.doubleValue() + percentY * height.doubleValue());
    }

    public void center(double xc, double yc) {
        translate(xc - xmin.add(xmax).divide(TWO, RM).doubleValue(), yc - ymin.add(ymax).divide(TWO, RM).doubleValue());
    }

    public MandelbrotModel duplicate() {
        MandelbrotModel copy = new MandelbrotModel();
        copy.xmin = BigDecimal.valueOf(xmin.doubleValue());
        copy.xmax = BigDecimal.valueOf(xmax.doubleValue());
        copy.ymin = BigDecimal.valueOf(ymin.doubleValue());
        copy.ymax = BigDecimal.valueOf(ymax.doubleValue());
        copy.maxIterations = maxIterations;
        copy.highPrecision = highPrecision;
        copy.palette = palette;
        copy.paletteMapping = paletteMapping;
        copy.mandelbrotColor = mandelbrotColor;
        copy.paletteColors = paletteColors;
        return copy;
    }
}
