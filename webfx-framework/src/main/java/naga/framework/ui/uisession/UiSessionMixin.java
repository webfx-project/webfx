package naga.framework.ui.uisession;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableBooleanValue;
import naga.framework.services.authz.mixin.ObservableUserAuthorizationFactory;
import naga.framework.services.authz.mixin.ObservableUserAuthorizationFactoryMixin;

/**
 * @author Bruno Salmon
 */
public interface UiSessionMixin extends UiSession, ObservableUserAuthorizationFactoryMixin {

    UiSession getUiSession();

    @Override
    default ObservableUserAuthorizationFactory getAuthorizationFactory() {
        return getUiSession();
    }

    default Property<Object> userPrincipalProperty() {
        return getUiSession().userPrincipalProperty();
    }

    default void setUserPrincipal(Object user) {
        getUiSession().setUserPrincipal(user);
    }

    default ObservableBooleanValue loggedInProperty() {
        return getUiSession().loggedInProperty();
    }

    default boolean isLoggedIn() {
        return getUiSession().isLoggedIn();
    }

}
