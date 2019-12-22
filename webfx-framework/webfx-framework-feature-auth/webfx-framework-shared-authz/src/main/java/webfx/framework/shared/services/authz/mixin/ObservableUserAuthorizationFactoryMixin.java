package webfx.framework.shared.services.authz.mixin;

import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public interface ObservableUserAuthorizationFactoryMixin extends ObservableUserAuthorizationFactory, AuthorizationFactoryMixin {

    @Override
    ObservableUserAuthorizationFactory getAuthorizationFactory();

    @Override
    default ObservableValue userPrincipalProperty() {
        return getAuthorizationFactory().userPrincipalProperty();
    }

    @Override
    default Object getUserPrincipal() {
        return getAuthorizationFactory().getUserPrincipal();
    }

    default ObservableBooleanValue authorizedOperationProperty(Factory operationRequestFactory) {
        return getAuthorizationFactory().authorizedOperationProperty(operationRequestFactory);
    }

}
