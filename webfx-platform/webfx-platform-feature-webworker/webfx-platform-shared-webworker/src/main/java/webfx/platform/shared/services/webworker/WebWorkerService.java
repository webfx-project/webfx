package webfx.platform.shared.services.webworker;

import webfx.platform.shared.services.webworker.spi.WorkerServiceProvider;
import webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WebWorkerService {

    public static WorkerServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(WorkerServiceProvider.class, () -> ServiceLoader.load(WorkerServiceProvider.class));
    }

    public static WebWorker createWorker(String scriptUrl) {
        return getProvider().createWorker(scriptUrl);
    }

    public static WebWorker createWorker(Class<? extends JavaCodedWebWorkerBase> javaCodedWorkerClass) {
        return getProvider().createWorker(javaCodedWorkerClass);
    }

}
