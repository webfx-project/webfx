package dev.webfx.platform.shared.services.webworker.spi;

import dev.webfx.platform.shared.services.webworker.WebWorker;
import dev.webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;

/**
 * @author Bruno Salmon
 */
public interface WorkerServiceProvider {

     WebWorker createWorker(String scriptUrl);

     WebWorker createWorker(Class<? extends JavaCodedWebWorkerBase> javaCodedWorkerClass);
}
