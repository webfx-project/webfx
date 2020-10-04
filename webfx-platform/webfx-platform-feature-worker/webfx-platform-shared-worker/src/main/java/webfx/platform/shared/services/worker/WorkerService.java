package webfx.platform.shared.services.worker;

import webfx.platform.shared.services.worker.spi.WorkerServiceProvider;
import webfx.platform.shared.services.worker.spi.abstrimpl.JavaApplicationWorker;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public class WorkerService {

    public static WorkerServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(WorkerServiceProvider.class, () -> ServiceLoader.load(WorkerServiceProvider.class));
    }

    public static Worker createWorker(String scriptUrl) {
        return getProvider().createWorker(scriptUrl);
    }

    public static Worker createWorker(Class<? extends JavaApplicationWorker> javaCodedWorkerClass) {
        return getProvider().createWorker(javaCodedWorkerClass);
    }

}
