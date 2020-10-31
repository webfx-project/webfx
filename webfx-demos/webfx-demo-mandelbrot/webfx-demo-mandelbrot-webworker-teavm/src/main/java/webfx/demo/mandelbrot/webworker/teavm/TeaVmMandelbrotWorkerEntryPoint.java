package webfx.demo.mandelbrot.webworker.teavm;


import webfx.demo.mandelbrot.webworker.MandelbrotWebWorker;
import webfx.platform.teavm.services.webworker.spi.impl.TeaVmRunningWebWorker;

/**
 * @author Bruno Salmon
 */
public class TeaVmMandelbrotWorkerEntryPoint {

    public static void main(String[] args) {
        TeaVmRunningWebWorker.executeJavaCodedWorker(new MandelbrotWebWorker());
    }

}
