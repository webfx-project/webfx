package webfx.demo.mandelbrot.math;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public final class MandelbrotMath {

    // The heart Mandelbrot computation algorithm

    public static int computeMandelbrotPointIterations(final double mx, final double my, final int maxIterations) {
        double zx = mx, zy = my;
        int iterations = 0;
        while (iterations < maxIterations) {
            double zx2 = zx * zx, zy2 = zy * zy;
            if (zx2 + zy2 >= 8)
                break;
            double new_zx = zx2 - zy2 + mx;
            zy = 2 * zx * zy + my;
            zx = new_zx;
            iterations++;
        }
        return iterations;
    }

    private final static ThreadLocal<MandelbrotPoint> mandelbrotPointThreadLocal = new ThreadLocal<>();
    private final static ThreadLocal<MandelbrotModel> mandelbrotModelThreadLocal = new ThreadLocal<>();

    public static void init(int width, int height, int placeIndex, int frameIndex) {
        mandelbrotPointThreadLocal.remove();
        MandelbrotModel model = mandelbrotModelThreadLocal.get();
        if (model == null || model.placeIndex != placeIndex || frameIndex < model.frameIndex) {
            model = MandelbrotModel.ofViewport(MandelbrotPlaces.PLACES[placeIndex]);
            model.placeIndex = placeIndex;
            model.adjustAspect(width, height);
            mandelbrotModelThreadLocal.set(model);
        }
        while (model.frameIndex < frameIndex) {
            model.moveToNextFrame();
            model.frameIndex++;
        }
    }

    public static int[] createLinePixelResultStorage(int canvasWidth, int[] pixelIterations) {
        if (pixelIterations != null && pixelIterations.length == canvasWidth)
            return pixelIterations;
        return new int[canvasWidth];
    }

    public static void computeLinePixelIterations(int cy, int[] pixelIterations) {
        int cx = 0;
        MandelbrotModel model = mandelbrotModelThreadLocal.get();
        while (cx < model.width)
            computeAndStorePixelResult(cx++, cy, pixelIterations);
    }

    public static int computeAndStorePixelResult(int cx, int cy, int[] pixelIterations) {
        MandelbrotPoint mbp = convertCanvasPixelToModelPoint(cx, cy);
        MandelbrotModel model = mandelbrotModelThreadLocal.get();
        int count = MandelbrotMath.computeMandelbrotPointIterations(mbp.x, mbp.y, model.maxIterations);
        pixelIterations[cx] = count;
        return count;
    }

    private static MandelbrotPoint convertCanvasPixelToModelPoint(int cx, int cy) {
        MandelbrotModel model = mandelbrotModelThreadLocal.get();
        MandelbrotPoint mbp = mandelbrotPointThreadLocal.get();
        if (mbp == null) {
            mandelbrotPointThreadLocal.set(mbp = new MandelbrotPoint());
            mbp.xmin_d = model.xmin.doubleValue();
            mbp.xmax_d = model.xmax.doubleValue();
            mbp.dy = null;
            mbp.y = mbp.cpy = Double.MAX_VALUE;
        }
        if (mbp.yval == null) {
            mbp.dx = (mbp.xmax_d - mbp.xmin_d) / (model.width - 1);
            mbp.dy = model.ymax.subtract(model.ymin).divide(new BigDecimal(model.height - 1), model.ymax.scale(), BigDecimal.ROUND_HALF_EVEN);
        }
        if (cy != mbp.cpy) {
            mbp.yval = model.ymax.subtract(mbp.dy.multiply(BigDecimal.valueOf(mbp.cpy = cy)));
            mbp.yval_d = mbp.yval.doubleValue();
        }
        mbp.x = mbp.xmin_d + mbp.dx * cx;
        mbp.y = mbp.yval_d;
        return mbp;
    }
}
