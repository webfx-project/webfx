package webfx.demo.mandelbrot.worker.teavm;


import webfx.demo.mandelbrot.worker.MandelbrotWorker;
import webfx.platform.teavm.services.worker.spi.impl.TeaVmRunningWorker;

/**
 * @author Bruno Salmon
 */
public class TeaVmMandelbrotWorkerEntryPoint {

    public static void main(String[] args) {
        TeaVmRunningWorker.executeJavaCodedWorker(new MandelbrotWorker());
    }

}
