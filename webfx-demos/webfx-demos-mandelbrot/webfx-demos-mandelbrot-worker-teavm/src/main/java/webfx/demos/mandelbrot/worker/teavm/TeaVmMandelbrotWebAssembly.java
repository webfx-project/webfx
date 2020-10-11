package webfx.demos.mandelbrot.worker.teavm;

import org.teavm.interop.Export;
import org.teavm.interop.Import;
import webfx.demos.mandelbrot.computation.MandelbrotComputation;
import webfx.demos.mandelbrot.computation.MandelbrotPoint;

import java.math.BigDecimal;

/**
 * @author Bruno Salmon
 */
public class TeaVmMandelbrotWebAssembly {

    public static void main(String[] args) {
    }

    @Export(name = "computeLinePixelIterations")
    public static void computeLinePixelIterations(double cy, double width, double height, double xmin, double xmax, double ymin, double ymax, int maxIterations) {
        //System.out.println("cy = " + cy + ", width = " + width + ", height = " + height + ", xmin = " + xmin + ", xmax = " + xmax + ", ymin = " + ymin + ", ymax = " + ymax + ", maxIterations = " + maxIterations);
        BigDecimal xMin = BigDecimal.valueOf(xmin);
        BigDecimal xMax = BigDecimal.valueOf(xmax);
        BigDecimal yMin = BigDecimal.valueOf(ymin);
        BigDecimal yMax = BigDecimal.valueOf(ymax);
        double cx = 0;
        while (cx < width) {
            // Passing the canvas pixel for the pixel color computation
            MandelbrotPoint mbp = MandelbrotComputation.convertCanvasPixelToModelPoint(cx, cy, width, height, xMin, xMax, yMin, yMax);
            int count = MandelbrotComputation.computeModelPointValue(mbp.x, mbp.y, maxIterations);
            setPixelIteration((int) cx++, count);
        }
    }

    @Import(module = "mandelbrot", name = "setPixelIteration")
    private static native void setPixelIteration(int x, int count);

}
