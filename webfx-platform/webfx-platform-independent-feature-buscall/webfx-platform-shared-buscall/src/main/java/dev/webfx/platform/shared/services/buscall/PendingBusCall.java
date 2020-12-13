package dev.webfx.platform.shared.services.buscall;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import dev.webfx.platform.shared.util.async.AsyncResult;
import dev.webfx.platform.shared.util.async.FutureImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class PendingBusCall<T> extends FutureImpl<T> {

    private static final List<PendingBusCall> pendingCalls = new ArrayList<>();
    private static final Property<Integer> pendingCallsCountProperty = new SimpleObjectProperty<>(0);
    // Note: this is the only javafx property used so far in the Platform module
    // TODO: decide if we keep it or replace it with something else to remove the dependency to javafx bindings
    public static Property<Integer> pendingCallsCountProperty() {
        return pendingCallsCountProperty;
    }

    PendingBusCall() {
        updatePendingCalls(true);
    }

    void onBusCallResult(AsyncResult<BusCallResult<T>> busCallAsyncResult) {
        // Getting the result of the bus call that needs to be returned back to the initial caller
        Object result = busCallAsyncResult.succeeded() ? busCallAsyncResult.result().getTargetResult() : busCallAsyncResult.cause();
        // Does it come from an asynchronous operation? (which returns an AsyncResult instance)
        if (result instanceof AsyncResult) { // if yes
            AsyncResult<T> ar = (AsyncResult<T>) result;
            // What needs to be returned is the successful result (if succeeded) or the exception (if failed)
            result = ar.succeeded() ? ar.result() : ar.cause();
        }
        // Now the result object is either the successful result or the exception whatever the nature of the operation (asynchronous or synchronous)
        if (result instanceof Throwable) // if it is an exception
            fail((Throwable) result); // we finally mark the pending call as failed and return that exception
        else // otherwise it is as successful result
            complete((T) result); // so we finally mark the pending call as complete and return that result (in the expected class result)
        // Updating the pending calls property
        updatePendingCalls(false);
    }

    private void updatePendingCalls(boolean addition) {
        if (addition)
            pendingCalls.add(this);
        else
            pendingCalls.remove(this);
        pendingCallsCountProperty.setValue(pendingCalls.size());
    }
}
