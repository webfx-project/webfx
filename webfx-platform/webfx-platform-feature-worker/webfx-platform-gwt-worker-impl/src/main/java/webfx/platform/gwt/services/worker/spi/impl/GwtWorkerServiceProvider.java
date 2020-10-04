package webfx.platform.gwt.services.worker.spi.impl;

import webfx.platform.shared.services.worker.spi.abstrimpl.JavaApplicationWorker;
import webfx.platform.shared.services.worker.Worker;
import webfx.platform.shared.services.worker.spi.WorkerServiceProvider;

/**
 * @author Bruno Salmon
 */
public class GwtWorkerServiceProvider implements WorkerServiceProvider {

    @Override
    public Worker createWorker(String scriptUrl) {
        return new GwtWorker(scriptUrl);
    }

    @Override
    public Worker createWorker(Class<? extends JavaApplicationWorker> javaCodedWorkerClass) {
        return createWorker(javaCodedWorkerClass.getName() + ".js");
    }
}
