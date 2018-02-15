package naga.framework.ui.authz;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.platform.services.auth.spi.authz.User;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface UiUserMixin extends UiUser {

    UiUser getUiUser();

    default Property<User> userProperty() {
        return getUiUser().userProperty();
    }

    @Override
    default User getUser() {
        return getUiUser().getUser();
    }

    default void setUser(User user) {
        getUiUser().setUser(user);
    }

    default ObservableBooleanValue loggedInProperty() {
        return getUiUser().loggedInProperty();
    }

    default boolean isLoggedIn() {
        return getUiUser().isLoggedIn();
    }

    default ObservableBooleanValue authorizedProperty(Object authority) {
        return getUiUser().authorizedProperty(authority);
    }

    @Override
    default ObservableBooleanValue authorizedProperty(ObservableValue authorityProperty) {
        return getUiUser().authorizedProperty(authorityProperty);
    }

    @Override
    default Future<Boolean> isAuthorized(Object authority) {
        return getUiUser().isAuthorized(authority);
    }
}
