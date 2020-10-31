package webfx.platform.shared.services.webworker.spi;

import webfx.platform.shared.services.webworker.WebWorker;
import webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;

/**
 * @author Bruno Salmon
 */
public interface WorkerServiceProvider {

     WebWorker createWorker(String scriptUrl);

     WebWorker createWorker(Class<? extends JavaCodedWebWorkerBase> javaCodedWorkerClass);
}
