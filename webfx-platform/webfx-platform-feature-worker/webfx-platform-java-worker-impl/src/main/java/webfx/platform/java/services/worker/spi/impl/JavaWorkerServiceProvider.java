package webfx.platform.java.services.worker.spi.impl;

import webfx.platform.shared.services.worker.spi.abstrimpl.JavaApplicationWorker;
import webfx.platform.shared.services.worker.Worker;
import webfx.platform.shared.services.worker.spi.WorkerServiceProvider;

/**
 * @author Bruno Salmon
 */
public class JavaWorkerServiceProvider implements WorkerServiceProvider {

    @Override
    public Worker createWorker(String scriptUrl) {
        return new JavaWorker(scriptUrl);
    }

    @Override
    public Worker createWorker(Class<? extends JavaApplicationWorker> javaCodedWorkerClass) {
        return new JavaWorker(javaCodedWorkerClass);
    }
}
