package dev.webfx.platform.shared.services.webworker.pool;

import dev.webfx.platform.shared.services.webworker.WebWorker;
import dev.webfx.platform.shared.services.webworker.WebWorkerService;
import dev.webfx.platform.shared.services.webworker.spi.base.DelegatingWebWorker;
import dev.webfx.platform.shared.services.webworker.spi.base.JavaCodedWebWorkerBase;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Bruno Salmon
 */
public class WebWorkerPool {

    private final Class<? extends JavaCodedWebWorkerBase> workerClass;
    private final Queue<WebWorker> pool = new LinkedList<>();

    public WebWorkerPool(Class<? extends JavaCodedWebWorkerBase> workerClass) {
        this.workerClass = workerClass;
    }

    public WebWorker getWorker() {
        WebWorker webWorker = pool.poll();
        if (webWorker == null)
            webWorker = new PooledWebWorker();
        return webWorker;
    }

    public void releaseWorker(WebWorker webWorker) {
        pool.add(webWorker);
    }

    private class PooledWebWorker extends DelegatingWebWorker {

        public PooledWebWorker() {
            super(WebWorkerService.createWorker(workerClass));
        }

        @Override
        public void terminate() {
            releaseWorker(this);
        }
    }
}
