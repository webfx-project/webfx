package dev.webfx.platform.shared.util.async;

import dev.webfx.platform.shared.util.tuples.Unit;

import java.util.function.IntFunction;

/**
 * @author Bruno Salmon
 */
public final class Batch<A> {

    private final A[] array;

    public Batch(A[] array) {
        this.array = array;
    }

    public A[] getArray() {
        return array;
    }

    public <R> Future<Batch<R>> executeParallel(IntFunction<R[]> arrayGenerator, AsyncFunction<A, R> asyncFunction) {
        return executeParallel(Future.future(), arrayGenerator, asyncFunction);
    }

    public <R> Future<Batch<R>> executeParallel(Future<Batch<R>> future, IntFunction<R[]> arrayGenerator, AsyncFunction<A, R> asyncFunction) {
        int n = array.length, i = 0;
        R[] results = arrayGenerator.apply(n);
        Unit<Integer> responseCounter = new Unit<>(0);
        for (A argument : getArray()) {
            int index = i++;
            asyncFunction.apply(argument).setHandler(asyncResult -> {
                if (!future.isComplete()) {
                    if (asyncResult.failed())
                        future.fail(asyncResult.cause());
                    else {
                        results[index] = asyncResult.result();
                        responseCounter.set(responseCounter.get() + 1);
                        if (responseCounter.get() == n)
                            future.complete(new Batch<>(results));
                    }
                }
            });
        }
        return future;
    }

    public <R> Future<Batch<R>> executeSerial(IntFunction<R[]> arrayGenerator, AsyncFunction<A, R> asyncFunction) {
        return executeSerial(Future.future(), arrayGenerator, asyncFunction);
    }

    public <R> Future<Batch<R>> executeSerial(Future<Batch<R>> future, IntFunction<R[]> arrayGenerator, AsyncFunction<A, R> asyncFunction) {
        int n = array.length;
        R[] results = arrayGenerator.apply(n);
        Unit<Integer> responseCounter = new Unit<>(0);
        Unit<Handler<AsyncResult<R>>> handlerUnit = new Unit<>();
        handlerUnit.set(asyncResult -> {
            if (!future.isComplete()) {
                if (asyncResult.failed())
                    future.fail(asyncResult.cause());
                else {
                    int count = responseCounter.get();
                    results[count] = asyncResult.result();
                    responseCounter.set(++count);
                    if (count < n)
                        asyncFunction.apply(getArray()[count]).setHandler(handlerUnit.get());
                    else
                        future.complete(new Batch<>(results));
                }
            }
        });
        asyncFunction.apply(getArray()[0]).setHandler(handlerUnit.get());
        return future;
    }

    public <R> Future<Batch<R>> executeIfSingularBatch(IntFunction<R[]> arrayGenerator, AsyncFunction<A, R> asyncFunction) {
        if (array.length > 1)
            return null;
        Future<Batch<R>> future = Future.future();
        executeIfSingularBatch(future, arrayGenerator, asyncFunction);
        return future;
    }

    public <R> boolean executeIfSingularBatch(Future<Batch<R>> future, IntFunction<R[]> arrayGenerator, AsyncFunction<A, R> asyncFunction) {
        int n = array.length;
        if (n > 1)
            return false;
        R[] results = arrayGenerator.apply(n);
        if (n == 0)
            future.complete(new Batch<>(results));
        else
            asyncFunction.apply(array[0]).setHandler(asyncResult -> {
                if (!future.isComplete()) {
                    if (asyncResult.failed())
                        future.fail(asyncResult.cause());
                    else {
                        results[0] = asyncResult.result();
                        future.complete(new Batch<>(results));
                    }
                }
            });
        return true;
    }
}