package webfx.platform.bus.call;

import webfx.platform.bus.Bus;

/**
 * Usage:
 *
 * try (ThreadLocalBusContext context = ThreadLocalBusContext.open(threadLocalBus)) {
 *      ...
 *      any call to Platform.bus() will return threadLocalBus instead of the default platform bus
 *      ...
 * }
 *
 * @author Bruno Salmon
 */
public class ThreadLocalBusContext implements AutoCloseable {

    private static ThreadLocal<Bus> busThreadLocal = new ThreadLocal<>();

    private final Bus previousBus = busThreadLocal.get();

    private ThreadLocalBusContext(Bus threadLocalBus) {
        busThreadLocal.set(threadLocalBus);
    }

    @Override
    public void close() {
        busThreadLocal.set(previousBus);
    }

    public static ThreadLocalBusContext open(Bus threadLocalBus) {
        return threadLocalBus == null ? null : new ThreadLocalBusContext(threadLocalBus);
    }

    public static Bus getThreadLocalBus() {
        return busThreadLocal.get();
    }
}
