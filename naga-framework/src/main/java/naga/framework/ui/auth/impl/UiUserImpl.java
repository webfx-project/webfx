package naga.framework.ui.auth.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.framework.ui.auth.UiUser;
import naga.platform.services.auth.spi.User;

/**
 * @author Bruno Salmon
 */
public class UiUserImpl implements UiUser {

    private Property<User> userProperty = new SimpleObjectProperty<>();
    @Override
    public Property<User> userProperty() {
        return userProperty;
    }

    @Override
    public Property<Boolean> authorizedProperty(Object authority) {
        return null; // Not yet implemented
    }
}
