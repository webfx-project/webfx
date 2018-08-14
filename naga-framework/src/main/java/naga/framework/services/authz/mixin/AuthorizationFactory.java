package naga.framework.services.authz.mixin;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.framework.services.authz.AuthorizationRequest;
import naga.util.async.Future;
import naga.util.function.Factory;
import naga.util.function.Function;

/**
 * @author Bruno Salmon
 */
public interface AuthorizationFactory extends HasUserPrincipal {

    default <Rq, Rs> AuthorizationRequest<Rq, Rs> newAuthorizationRequest() {
        return new AuthorizationRequest<Rq, Rs>().setUserPrincipal(getUserPrincipal());
    }

    default <Rq, Rs> AuthorizationRequest<Rq, Rs> newAuthorizationRequest(Rq operationRequest) {
        return this.<Rq, Rs>newAuthorizationRequest().setOperationRequest(operationRequest);
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

    default <C> ObservableBooleanValue authorizedOperationProperty(ObservableValue<C> observableContext, Function<C, ?> operationRequestFactory ) {
        return AuthorizationUtil.authorizedOperationProperty(operationRequestFactory, this::isAuthorized, observableContext, this instanceof HasUserPrincipalProperty ? ((HasUserPrincipalProperty) this).userPrincipalProperty() : null);
    }

}
