package naga.commons.util.async;

import naga.commons.util.tuples.Unit;

import java.lang.reflect.Array;

/**
 * @author Bruno Salmon
 */
public class Batch<A> {

    private final A[] array;

    public Batch(A[] array) {
        this.array = array;
    }

    public A[] getArray() {
        return array;
    }

    public <R> Future<Batch<R>> executeParallel(Class<R> expectedResultClass, AsyncFunction<A, R> asyncFunction) {
        return executeParallel(Future.future(), expectedResultClass, asyncFunction);
    }

    public <R> Future<Batch<R>> executeParallel(Future<Batch<R>> future, Class<R> expectedResultClass, AsyncFunction<A, R> asyncFunction) {
        int n = array.length, i = 0;
        R[] results = (R[]) Array.newInstance(expectedResultClass, n);
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

    public <R> Future<Batch<R>> executeSerial(Class<R> expectedResultClass, AsyncFunction<A, R> asyncFunction) {
        return executeSerial(Future.future(), expectedResultClass, asyncFunction);
    }

    public <R> Future<Batch<R>> executeSerial(Future<Batch<R>> future, Class<R> expectedResultClass, AsyncFunction<A, R> asyncFunction) {
        int n = array.length;
        R[] results = (R[]) Array.newInstance(expectedResultClass, n);
        Unit<Integer> responseCounter = new Unit<>(0);
        Unit<Handler<AsyncResult<R>>> handlerUnit = new Unit<>();
        handlerUnit.set(asyncResult -> {
            if (!future.isComplete()) {
                if (asyncResult.failed())
                    future.fail(asyncResult.cause());
                else {
                    results[responseCounter.get()] = asyncResult.result();
                    responseCounter.set(responseCounter.get() + 1);
                    if (responseCounter.get() < n)
                        asyncFunction.apply(getArray()[0]).setHandler(handlerUnit.get());
                    else
                        future.complete(new Batch<>(results));
                }
            }
        });
        asyncFunction.apply(getArray()[0]).setHandler(handlerUnit.get());
        return future;
    }

    public <R> Future<Batch<R>> executeIfSingularBatch(Class<R> expectedResultClass, AsyncFunction<A, R> asyncFunction) {
        if (array.length > 1)
            return null;
        Future<Batch<R>> future = Future.future();
        executeIfSingularBatch(future, expectedResultClass, asyncFunction);
        return future;
    }

    public <R> boolean executeIfSingularBatch(Future<Batch<R>> future, Class<R> expectedResultClass, AsyncFunction<A, R> asyncFunction) {
        int n = array.length;
        if (n > 1)
            return false;
        R[] results = (R[]) Array.newInstance(expectedResultClass, n);
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