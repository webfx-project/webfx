package webfx.demos.raytracer.worker.teavm;

import webfx.demos.raytracer.worker.RayTracerWorker;
import webfx.platform.teavm.services.worker.spi.impl.TeaVmRunningWorker;

/**
 * @author Bruno Salmon
 */
public class TeaVmRayTracerWorkerEntryPoint {

    public static void main(String[] args) {
        TeaVmRunningWorker.executeJavaCodedWorker(new RayTracerWorker());
    }
}
