package webfx.demo.mandelbrot.math;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class MandelbrotPlace extends MandelbrotViewport {

    public String rgbForMandelbrot;
    public String linearGradient;
    public boolean hsbInterpolation;
    public int paletteMappingLength;
    public int paletteMappingOffset;
    public int thumbnailFrame;
    public int lastFrame;
    long totalIterations;

    public MandelbrotPlace(double xmin, double xmax, double ymin, double ymax, String rgbForMandelbrot, String linearGradient, boolean hsbInterpolation, int paletteMappingLength, int paletteMappingOffset, int maxIterations, int thumbnailFrame, int lastFrame, long totalIterations) {
        this(BigDecimal.valueOf(xmin), BigDecimal.valueOf(xmax), BigDecimal.valueOf(ymin), BigDecimal.valueOf(ymax), rgbForMandelbrot, linearGradient, hsbInterpolation, paletteMappingLength, paletteMappingOffset, maxIterations, thumbnailFrame, lastFrame, totalIterations);
    }

    public MandelbrotPlace(BigDecimal xmin, BigDecimal xmax, BigDecimal ymin, BigDecimal ymax, String rgbForMandelbrot, String linearGradient, boolean hsbInterpolation, int paletteMappingLength, int paletteMappingOffset, int maxIterations, int thumbnailFrame, int lastFrame, long totalIterations) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.rgbForMandelbrot = rgbForMandelbrot;
        this.linearGradient = linearGradient;
        this.hsbInterpolation = hsbInterpolation;
        this.paletteMappingLength = paletteMappingLength;
        this.paletteMappingOffset = paletteMappingOffset;
        this.maxIterations = maxIterations;
        this.thumbnailFrame = thumbnailFrame;
        this.lastFrame = lastFrame;
        this.totalIterations = totalIterations;
    }
}
