package webfx.platform.shared.services.worker.pool;

import webfx.platform.shared.services.worker.Worker;
import webfx.platform.shared.services.worker.WorkerService;
import webfx.platform.shared.services.worker.spi.base.DelegatingWorker;
import webfx.platform.shared.services.worker.spi.base.JavaCodedWorkerBase;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Bruno Salmon
 */
public class WorkerPool<T extends JavaCodedWorkerBase> {

    private final Class<T> workerClass;
    private final Queue<Worker> pool = new LinkedList<>();

    public WorkerPool(Class<T> workerClass) {
        this.workerClass = workerClass;
    }

    public Worker getWorker() {
        Worker worker = pool.poll();
        if (worker == null)
            worker = new PooledWorker();
        return worker;
    }

    public void releaseWorker(Worker worker) {
        pool.add(worker);
    }

    private class PooledWorker extends DelegatingWorker {

        public PooledWorker() {
            super(WorkerService.createWorker(workerClass));
        }

        @Override
        public void terminate() {
            releaseWorker(this);
        }
    }
}
