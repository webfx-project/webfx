package naga.framework.ui.auth.impl;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import naga.framework.ui.auth.UiUser;
import naga.fx.properties.Properties;
import naga.platform.services.auth.spi.User;

import java.util.Objects;

/**
 * @author Bruno Salmon
 */
public class UiUserImpl implements UiUser {

    private Property<User> userProperty = new SimpleObjectProperty<>();
    private ObservableValue<Boolean> loggedInProperty = Properties.compute(userProperty, Objects::nonNull);

    @Override
    public Property<User> userProperty() {
        return userProperty;
    }

    @Override
    public ObservableValue<Boolean> loggedInProperty() {
        return loggedInProperty;
    }

    @Override
    public Property<Boolean> authorizedProperty(Object authority) {
        return null; // Not yet implemented
    }
}
