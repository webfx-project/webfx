package naga.framework.ui.auth.impl;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.util.Objects;
import naga.framework.ui.auth.UiUser;
import naga.fx.properties.Properties;
import naga.platform.services.auth.spi.User;
import naga.util.async.Future;


/**
 * @author Bruno Salmon
 */
public class UiUserImpl implements UiUser {

    private Property<User> userProperty = new SimpleObjectProperty<>();
    private ObservableBooleanValue loggedInProperty = BooleanExpression.booleanExpression(Properties.compute(userProperty, Objects::nonNull));

    @Override
    public Property<User> userProperty() {
        return userProperty;
    }

    @Override
    public ObservableBooleanValue loggedInProperty() {
        return loggedInProperty;
    }

    @Override
    public ObservableBooleanValue authorizedProperty(Object authority) {
        return null; // Not yet implemented
    }

    @Override
    public ObservableBooleanValue authorizedProperty(ObservableValue authorityProperty) {
        return null; // Not yet implemented
    }

    @Override
    public Future<Boolean> isAuthorized(Object authority) {
        return getUser().isAuthorized(authority)/*.compose(
                authorized -> authorizedProperty(authority).setValue(authorized),
                Future.future())*/;
    }
}
