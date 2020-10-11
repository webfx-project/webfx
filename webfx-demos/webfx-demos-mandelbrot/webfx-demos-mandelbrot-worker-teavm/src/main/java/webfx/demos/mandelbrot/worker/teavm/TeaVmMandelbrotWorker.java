package webfx.demos.mandelbrot.worker.teavm;


import webfx.demos.mandelbrot.worker.MandelbrotWorker;
import webfx.platform.teavm.services.worker.spi.impl.TeaVmRunningWorker;

/**
 * @author Bruno Salmon
 */
public class TeaVmMandelbrotWorker {

    public static void main(String[] args) {
        TeaVmRunningWorker.executeJavaCodedWorker(new MandelbrotWorker());
    }

}
