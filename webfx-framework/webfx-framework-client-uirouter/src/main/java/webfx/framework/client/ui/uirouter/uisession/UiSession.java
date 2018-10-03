package webfx.framework.client.ui.uirouter.uisession;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import webfx.framework.shared.services.authz.mixin.ObservableUserAuthorizationFactory;
import webfx.framework.client.ui.uirouter.uisession.impl.UiSessionImpl;

/**
 * @author Bruno Salmon
 */
public interface UiSession extends ObservableUserAuthorizationFactory {

    // Authentication aspect

    Property<Object> userPrincipalProperty(); // Made it writable by returning Property instead of ObservableValue

    default void setUserPrincipal(Object authenticatedUser) {
        userPrincipalProperty().setValue(authenticatedUser);
    }

    ObservableBooleanValue loggedInProperty();

    default boolean isLoggedIn() {
        return loggedInProperty().getValue();
    }

    static UiSession get() {
        return UiSessionImpl.getInstance();
    }
}
