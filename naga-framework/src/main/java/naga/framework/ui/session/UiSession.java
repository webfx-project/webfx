package naga.framework.ui.session;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.framework.spi.authz.AuthorizationRequest;
import naga.framework.ui.session.impl.UiSessionImpl;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UiSession extends HasUserPrincipal {

    // Authentication aspect

    Property<Object> userPrincipalProperty();

    @Override
    default Object getUserPrincipal() {
        return userPrincipalProperty().getValue();
    }

    default void setUserPrincipal(Object authenticatedUser) {
        userPrincipalProperty().setValue(authenticatedUser);
    }

    ObservableBooleanValue loggedInProperty();

    default boolean isLoggedIn() {
        return loggedInProperty().getValue();
    }

    // Authorization aspect

    ObservableBooleanValue authorizedProperty(Object operationAuthorizationRequest);

    ObservableBooleanValue authorizedProperty(ObservableValue operationAuthorizationRequestProperty);

    //@Override
    default Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        return new AuthorizationRequest<>()
                .setUserPrincipal(getUserPrincipal())
                .setOperationAuthorizationRequest(operationAuthorizationRequest)
                .isAuthorizedAsync();
    }

    static UiSession create() {
        return new UiSessionImpl();
    }
}
