package webfx.demos.mandelbrot.computation;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class MandelbrotComputation {

    private static ThreadLocal<MandelbrotPoint> mandelbrotPointThreadLocal = new ThreadLocal<>();

    public static void init() {
        mandelbrotPointThreadLocal = new ThreadLocal<>();
    }

    public static MandelbrotPoint convertCanvasPixelToModelPoint(double x, double y, double canvasWidth, double canvasHeight, MandelbrotViewport viewport) {
        return convertCanvasPixelToModelPoint(x, y, canvasWidth, canvasHeight, viewport.xmin, viewport.xmax, viewport.ymin, viewport.ymax);
    }

    public static MandelbrotPoint convertCanvasPixelToModelPoint(double x, double y, double canvasWidth, double canvasHeight, BigDecimal xmin, BigDecimal xmax, BigDecimal ymin, BigDecimal ymax) {
        MandelbrotPoint mbp = mandelbrotPointThreadLocal.get();
        if (mbp == null) {
            mandelbrotPointThreadLocal.set(mbp = new MandelbrotPoint());
            mbp.xmin_d = xmin.doubleValue();
            mbp.xmax_d = xmax.doubleValue();
            mbp.dy = null;
            mbp.y = mbp.cpy = Double.MAX_VALUE;
        }
        if (mbp.yval == null) {
            mbp.dx = (mbp.xmax_d - mbp.xmin_d) / (canvasWidth - 1);
            mbp.dy = ymax.subtract(ymin).divide(new BigDecimal(canvasHeight - 1), ymax.scale(), BigDecimal.ROUND_HALF_EVEN);
        }
        if (y != mbp.cpy) {
            mbp.yval = ymax.subtract(mbp.dy.multiply(new BigDecimal(mbp.cpy = y)));
            mbp.yval_d = mbp.yval.doubleValue();
        }
        mbp.x = mbp.xmin_d + mbp.dx * x;
        mbp.y = mbp.yval_d;
        return mbp;
    }

    public static int computeModelPointValue(double x, double y, int maxIterations) {
        double zx = x, zy = y;
        int count = 0;
        while (count < maxIterations
                && zx * zx + zy * zy < 8) {
            double new_zx = zx * zx - zy * zy + x;
            zy = 2 * zx * zy + y;
            zx = new_zx;
            count++;
        }
        return count;
    }

}
