package naga.framework.spi.authz.mixin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.framework.spi.authz.AuthorizationRequest;
import naga.util.async.Future;
import naga.util.function.Factory;
import naga.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationFactory extends HasUserPrincipal {

    default <O, R> AuthorizationRequest<O, R> newAuthorizationRequest() {
        return new AuthorizationRequest<O, R>().setUserPrincipal(getUserPrincipal());
    }

    default <O, R> AuthorizationRequest<O, R> newAuthorizationRequest(O operationRequest) {
        return this.<O, R>newAuthorizationRequest().setOperationRequest(operationRequest);
    }

    default Future<Boolean> isAuthorized(Object operationRequest) {
        return newAuthorizationRequest(operationRequest).isAuthorizedAsync();
    }

    default ObservableBooleanValue authorizedOperationProperty(Object operationRequest) {
        return authorizedOperationProperty(() -> operationRequest);
    }

    default ObservableBooleanValue authorizedOperationProperty(Factory operationRequestFactory) {
        return authorizedOperationProperty(new SimpleObjectProperty<>(), ignored -> operationRequestFactory.create());
    }

    default <T> ObservableBooleanValue authorizedOperationProperty(ObservableValue<T> observableContext, Function<T, ?> operationRequestFactory ) {
        return AuthorizationUtil.authorizedOperationProperty(observableContext, operationRequestFactory, this::isAuthorized);
    }

}
