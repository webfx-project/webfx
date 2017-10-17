package naga.framework.ui.auth;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import naga.util.async.Future;
import naga.framework.ui.auth.impl.UiUserImpl;
import naga.platform.services.auth.spi.User;
import naga.platform.services.auth.spi.UserMixin;

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

    ObservableValue<Boolean> loggedInProperty();

    default boolean isLoggedIn() {
        return loggedInProperty().getValue();
    }

    Property<Boolean> authorizedProperty(Object authority);

    @Override
    default Future<Boolean> isAuthorized(Object authority) {
        return getUser().isAuthorized(authority).compose(
                authorized -> authorizedProperty(authority).setValue(authorized),
                Future.future());
    }
}
