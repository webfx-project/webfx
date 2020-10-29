package webfx.demo.raytracer.webworker.teavm;

import webfx.demo.raytracer.webworker.RayTracerWebWorker;
import webfx.platform.teavm.services.webworker.spi.impl.TeaVmRunningWebWorker;

/**
 * @author Bruno Salmon
 */
public class TeaVmRayTracerWorkerEntryPoint {

    public static void main(String[] args) {
        TeaVmRunningWebWorker.executeJavaCodedWorker(new RayTracerWebWorker());
    }
}
