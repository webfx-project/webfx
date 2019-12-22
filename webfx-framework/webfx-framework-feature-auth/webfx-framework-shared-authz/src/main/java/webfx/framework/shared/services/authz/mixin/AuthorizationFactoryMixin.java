package webfx.framework.shared.services.authz.mixin;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import webfx.framework.shared.services.authz.AuthorizationRequest;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.function.Factory;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationFactoryMixin extends AuthorizationFactory {

    AuthorizationFactory getAuthorizationFactory();

    @Override
    default Object getUserPrincipal() {
        return getAuthorizationFactory().getUserPrincipal();
    }

    @Override
    default <Rq, Rs> AuthorizationRequest<Rq, Rs> newAuthorizationRequest() {
        return getAuthorizationFactory().newAuthorizationRequest();
    }

    @Override
    default <Rq, Rs> AuthorizationRequest<Rq, Rs> newAuthorizationRequest(Rq operationRequest) {
        return getAuthorizationFactory().newAuthorizationRequest(operationRequest);
    }

    @Override
    default Future<Boolean> isAuthorized(Object operationRequest) {
        return getAuthorizationFactory().isAuthorized(operationRequest);
    }

    @Override
    default ObservableBooleanValue authorizedOperationProperty(Object operationRequest) {
        return getAuthorizationFactory().authorizedOperationProperty(operationRequest);
    }

    @Override
    default ObservableBooleanValue authorizedOperationProperty(Factory operationRequestFactory) {
        return getAuthorizationFactory().authorizedOperationProperty(operationRequestFactory);
    }

    @Override
    default <T> ObservableBooleanValue authorizedOperationProperty(ObservableValue<T> observableContext, Function<T, ?> operationRequestFactory) {
        return getAuthorizationFactory().authorizedOperationProperty(observableContext, operationRequestFactory);
    }
}
