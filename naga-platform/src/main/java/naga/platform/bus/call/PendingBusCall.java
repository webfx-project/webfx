package naga.platform.bus.call;

import naga.commons.util.async.AsyncResult;
import naga.commons.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class PendingBusCall<T> extends Future<T> {

    void onBusCallResult(BusCallResult<T> busCallResult) {
        // Getting the result of the bus call that needs to be returned back to the initial caller
        Object result = busCallResult.getTargetResult();
        // Does it come from an asynchronous operation? (which returns an AsyncResult instance)
        if (result instanceof AsyncResult) { // if yes
            AsyncResult<T> asyncResult = (AsyncResult<T>) result;
            // What needs to be returned is the successful result (if succeeded) or the exception (if failed)
            result = asyncResult.succeeded() ? asyncResult.result() : asyncResult.cause();
        }
        // Now the result object is either the successful result or the exception whatever the nature of the operation (asynchronous or synchronous)
        if (result instanceof Throwable) // if it is an exception
            fail((Throwable) result); // we finally mark the pending call as failed and return that exception
        else // otherwise it is as successful result
            complete((T) result); // so we finally mark the pending call as complete and return that result (in the expected class result)
    }
}
