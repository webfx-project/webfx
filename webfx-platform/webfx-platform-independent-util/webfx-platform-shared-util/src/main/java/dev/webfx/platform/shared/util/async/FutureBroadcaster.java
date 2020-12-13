package dev.webfx.platform.shared.util.async;

import dev.webfx.platform.shared.util.function.Callable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Bruno Salmon
 */
public final class FutureBroadcaster<T> {

    private final Callable<Future<T>> sourceProducer;
    private Future<T> source;
    private final Collection<Future<T>> clients = new ArrayList<>();

    public FutureBroadcaster(Callable<Future<T>> sourceProducer) {
        this.sourceProducer = sourceProducer;
    }

    public FutureBroadcaster(Future<T> source) {
        sourceProducer = null;
        this.source = armSource(source);
    }

    private Future<T> armSource(Future<T> s) {
        s.setHandler(ar -> onSourceCompleted());
        return s;
    }

    private void onSourceCompleted() {
        synchronized (this) {
            for (Future<T> destination : clients)
                destination.complete(source);
            clients.clear();
            if (sourceProducer != null)
                source = null;
        }
    }

    public Future<T> newClient() {
        synchronized (this) {
            Future<T> newClient = Future.future();
            clients.add(newClient);
            if (source == null)
                source = armSource(sourceProducer.call());
            if (source.isComplete())
                onSourceCompleted();
            return newClient;
        }
    }

}
