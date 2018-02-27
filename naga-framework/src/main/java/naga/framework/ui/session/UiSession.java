package naga.framework.ui.session;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import naga.framework.spi.authz.mixin.ObservableUserAuthorizationFactory;
import naga.framework.ui.session.impl.UiSessionImpl;

/**
 * @author Bruno Salmon
 */
public interface UiSession extends ObservableUserAuthorizationFactory {

    // Authentication aspect

    Property<Object> userPrincipalProperty();

    default void setUserPrincipal(Object authenticatedUser) {
        userPrincipalProperty().setValue(authenticatedUser);
    }

    ObservableBooleanValue loggedInProperty();

    default boolean isLoggedIn() {
        return loggedInProperty().getValue();
    }

    static UiSession create() {
        return new UiSessionImpl();
    }
}
