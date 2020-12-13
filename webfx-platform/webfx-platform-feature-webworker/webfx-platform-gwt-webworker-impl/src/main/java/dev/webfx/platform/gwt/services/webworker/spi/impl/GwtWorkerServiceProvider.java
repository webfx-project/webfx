package dev.webfx.platform.gwt.services.webworker.spi.impl;

import dev.webfx.platform.shared.services.webworker.WebWorker;
import dev.webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;
import dev.webfx.platform.shared.services.webworker.spi.WorkerServiceProvider;

/**
 * @author Bruno Salmon
 */
public class GwtWorkerServiceProvider implements WorkerServiceProvider {

    @Override
    public WebWorker createWorker(String scriptUrl) {
        return new GwtWebWorker(scriptUrl);
    }

    @Override
    public WebWorker createWorker(Class<? extends JavaCodedWebWorkerBase> javaCodedWorkerClass) {
        return createWorker(javaCodedWorkerClass.getName() + ".js");
    }
}
