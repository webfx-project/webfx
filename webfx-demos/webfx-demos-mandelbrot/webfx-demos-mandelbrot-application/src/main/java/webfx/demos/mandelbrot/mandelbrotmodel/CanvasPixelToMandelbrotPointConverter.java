package webfx.demos.mandelbrot.mandelbrotmodel;

import webfx.demos.mandelbrot.canvastracer.CanvasPixel;
import webfx.demos.mandelbrot.canvastracer.model.CanvasPixelToModelPointConverter;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class CanvasPixelToMandelbrotPointConverter implements CanvasPixelToModelPointConverter {

    private MandelbrotModel model;
    private final ThreadLocal<MandelbrotPoint> mandelbrotPointThreadLocal = new ThreadLocal<>();

    public CanvasPixelToMandelbrotPointConverter() {
    }

    public CanvasPixelToMandelbrotPointConverter(MandelbrotModel model) {
        setModel(model);
    }

    @Override
    public Object convertCanvasPixelToModelPoint(CanvasPixel cp) {
        MandelbrotPoint mbp = mandelbrotPointThreadLocal.get();
        if (mbp == null) {
            mandelbrotPointThreadLocal.set(mbp = new MandelbrotPoint());
            mbp.model = model;
            mbp.xmin_d = model.xmin.doubleValue();
            mbp.xmax_d = model.xmax.doubleValue();
            mbp.dy = null;
            mbp.y = mbp.cpy = Double.MAX_VALUE;
        }
        if (mbp.yval == null) {
            mbp.dx = (mbp.xmax_d - mbp.xmin_d) / (cp.canvasWidth - 1);
            mbp.dy = mbp.model.ymax.subtract(mbp.model.ymin).divide(new BigDecimal(cp.canvasHeight -1), mbp.model.ymax.scale(), BigDecimal.ROUND_HALF_EVEN);
        }
        if (cp.y != mbp.cpy) {
            mbp.yval = model.ymax.subtract(mbp.dy.multiply(new BigDecimal(mbp.cpy = cp.y)));
            mbp.yval_d = mbp.yval.doubleValue();
        }
        mbp.x = mbp.xmin_d + mbp.dx*cp.x;
        mbp.y = mbp.yval_d;
        mbp.model = model;
        return mbp;
    }

    public void setModel(MandelbrotModel model) {
        this.model = model;
    }
}
