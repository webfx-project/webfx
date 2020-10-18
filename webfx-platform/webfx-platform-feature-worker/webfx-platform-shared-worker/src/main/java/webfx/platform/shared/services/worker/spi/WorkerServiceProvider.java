package webfx.platform.shared.services.worker.spi;

import webfx.platform.shared.services.worker.spi.base.JavaCodedWorkerBase;
import webfx.platform.shared.services.worker.Worker;

/**
 * @author Bruno Salmon
 */
public interface WorkerServiceProvider {

     Worker createWorker(String scriptUrl);

     Worker createWorker(Class<? extends JavaCodedWorkerBase> javaCodedWorkerClass);
}
