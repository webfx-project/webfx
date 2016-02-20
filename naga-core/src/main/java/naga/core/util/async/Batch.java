package naga.core.util.async;

import naga.core.util.Holder;

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

    public <R> Future<Batch<R>> execute(AsyncFunction<A, R> asyncFunction, Class<R> expectedResultClass) {
        Future<Batch<R>> future = Future.future();
        int n = array.length, i = 0;
        R[] results = (R[]) Array.newInstance(expectedResultClass, n);
        Holder<Integer> responseCounter = new Holder<>(0);
        for (A argument : getArray()) {
            int index = i++;
            asyncFunction.apply(argument).setHandler(asyncResult -> {
                if (!future.isComplete()) {
                    if (asyncResult.failed())
                        future.fail(asyncResult.cause());
                    else
                        results[index] = asyncResult.result();
                    responseCounter.set(responseCounter.get() + 1);
                    if (responseCounter.get() == n)
                        future.complete(new Batch<>(results));
                }
            });
        }
        return future;
    }

}
