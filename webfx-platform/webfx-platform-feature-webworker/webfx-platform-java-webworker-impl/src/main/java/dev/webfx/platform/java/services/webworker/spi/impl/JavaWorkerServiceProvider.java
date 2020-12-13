package dev.webfx.platform.java.services.webworker.spi.impl;

import dev.webfx.platform.shared.services.webworker.WebWorker;
import dev.webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;
import dev.webfx.platform.shared.services.webworker.spi.WorkerServiceProvider;

/**
 * @author Bruno Salmon
 */
public class JavaWorkerServiceProvider implements WorkerServiceProvider {

    @Override
    public WebWorker createWorker(String scriptUrl) {
        return new JavaWebWorker(scriptUrl);
    }

    @Override
    public WebWorker createWorker(Class<? extends JavaCodedWebWorkerBase> javaCodedWorkerClass) {
        return new JavaWebWorker(javaCodedWorkerClass);
    }
}
