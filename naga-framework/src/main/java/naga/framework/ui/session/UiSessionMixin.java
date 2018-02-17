package naga.framework.ui.session;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.platform.services.authz.User;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UiSessionMixin extends UiSession {

    UiSession getUiSession();

    default Property<User> userProperty() {
        return getUiSession().userProperty();
    }

    @Override
    default User getUser() {
        return getUiSession().getUser();
    }

    default void setUser(User user) {
        getUiSession().setUser(user);
    }

    default ObservableBooleanValue loggedInProperty() {
        return getUiSession().loggedInProperty();
    }

    default boolean isLoggedIn() {
        return getUiSession().isLoggedIn();
    }

    default ObservableBooleanValue authorizedProperty(Object operationAuthorizationRequest) {
        return getUiSession().authorizedProperty(operationAuthorizationRequest);
    }

    @Override
    default ObservableBooleanValue authorizedProperty(ObservableValue operationAuthorizationRequestProperty) {
        return getUiSession().authorizedProperty(operationAuthorizationRequestProperty);
    }

    @Override
    default Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        return getUiSession().isAuthorized(operationAuthorizationRequest);
    }
}
