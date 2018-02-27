package naga.framework.spi.authz.mixin;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.fx.spi.Toolkit;
import naga.util.async.AsyncFunction;
import naga.util.function.Function;

/**
 * @author Bruno Salmon
 */
public class AuthorizationUtil {

    public static <C, O> ObservableBooleanValue authorizedOperationProperty(ObservableValue<C> observableContext, Function<C, O> operationRequestFactory, AsyncFunction<O, Boolean> authorizationFunction) {
        return new BooleanBinding() {
            C context;
            Boolean value;
            { bind(observableContext); }

            @Override
            protected void onInvalidating() {
                C context = observableContext.getValue();
                if (this.context != context || value == null) {
                    value = false;
                    authorizationFunction.apply(operationRequestFactory.apply(context)).setHandler(ar -> {
                        this.context = context;
                        if (ar.succeeded())
                            Toolkit.get().scheduler().runInUiThread(() -> {
                                value = ar.result();
                                invalidate();
                            });
                    });
                }
            }

            @Override
            protected boolean computeValue() {
                if (value == null)
                    onInvalidating();
                return value;
            }
        };
    }

}
