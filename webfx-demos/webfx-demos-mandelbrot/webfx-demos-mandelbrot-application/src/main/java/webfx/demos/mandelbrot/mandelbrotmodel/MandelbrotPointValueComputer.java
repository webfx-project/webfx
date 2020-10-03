package webfx.demos.mandelbrot.mandelbrotmodel;

import webfx.demos.mandelbrot.canvastracer.model.ModelPointValueComputer;

/**
 * @author Bruno Salmon
 */
public class MandelbrotPointValueComputer implements ModelPointValueComputer {

    private final ThreadLocal<MandelbrotPointValue> mandelbrotPointValueThreadLocal = new ThreadLocal<>();

    @Override
    public Object computeModelPointValue(Object mp) {
        MandelbrotPoint mbp = (MandelbrotPoint) mp;
        MandelbrotModel model = mbp.model;
        double x = mbp.x, y = mbp.y, zx = x, zy = y;
        int count = 0, maxIterations = model.maxIterations;
        while (count < maxIterations
                && zx*zx + zy*zy < 8) {
            double new_zx = zx*zx - zy*zy + x;
            zy = 2*zx*zy + y;
            zx = new_zx;
            count++;
        }
        MandelbrotPointValue mbpv = mandelbrotPointValueThreadLocal.get();
        if (mbpv == null)
            mandelbrotPointValueThreadLocal.set(mbpv = new MandelbrotPointValue());
        mbpv.model = model;
        mbpv.count = count;
        return mbpv;
    }
}
