package naga.framework.ui.authz;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.util.async.Future;
import naga.framework.ui.authz.impl.UiUserImpl;
import naga.platform.services.auth.spi.authz.User;
import naga.platform.services.auth.spi.authz.UserMixin;

/**
 * @author Bruno Salmon
 */
public interface UiUser extends UserMixin {

    static UiUser create() {
        return new UiUserImpl();
    }

    Property<User> userProperty();

    @Override
    default User getUser() {
        return userProperty().getValue();
    }

    default void setUser(User user) {
        userProperty().setValue(user);
    }

    ObservableBooleanValue loggedInProperty();

    default boolean isLoggedIn() {
        return loggedInProperty().getValue();
    }

    ObservableBooleanValue authorizedProperty(Object operationAuthorizationRequest);

    ObservableBooleanValue authorizedProperty(ObservableValue operationAuthorizationRequestProperty);

    @Override
    default Future<Boolean> isAuthorized(Object operationAuthorizationRequest) {
        User user = getUser();
        return user != null ? user.isAuthorized(operationAuthorizationRequest) : Future.succeededFuture(false);
    }
}
