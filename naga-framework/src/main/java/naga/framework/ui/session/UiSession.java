package naga.framework.ui.session;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.framework.ui.session.impl.UiSessionImpl;
import naga.framework.spi.authz.AuthorizationService;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UiSession {

    // Authentication aspect

    Property<Object> userPrincipalProperty();

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
        AuthorizationService authorizationService = AuthorizationService.get();
        return authorizationService != null ? authorizationService.isAuthorized(operationAuthorizationRequest, getUserPrincipal()) : Future.succeededFuture(false);
    }

    static UiSession create() {
        return new UiSessionImpl();
    }
}
