package naga.framework.spi.authz.mixin;

import javafx.beans.value.ObservableBooleanValue;
import naga.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public interface ObservableUserAuthorizationFactory extends AuthorizationFactory, HasUserPrincipalProperty {

    @Override
    default ObservableBooleanValue authorizedOperationProperty(Factory operationRequestFactory) {
        return authorizedOperationProperty(userPrincipalProperty(), ignored -> operationRequestFactory.create());
    }


}
