package naga.framework.ui.authz.impl;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.framework.ui.authz.UiUser;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.platform.services.auth.spi.authz.User;
import naga.util.Objects;


/**
 * @author Bruno Salmon
 */
public class UiUserImpl implements UiUser {

    private final Property<User> userProperty = new SimpleObjectProperty<>();
    private final ObservableBooleanValue loggedInProperty = BooleanExpression.booleanExpression(Properties.compute(userProperty, Objects::nonNull));

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
        return new BooleanBinding() {
            User user;
            boolean value;
            { bind(userProperty); }

            @Override
            protected void onInvalidating() {
                if (user != getUser()) {
                    value = false;
                    user = getUser();
                    user.isAuthorized(authority).setHandler(ar -> {
                        if (ar.succeeded() && ar.result())
                            Toolkit.get().scheduler().runInUiThread(() -> {
                                value = true;
                                invalidate();
                            });
                    });
                }
            }

            @Override
            protected boolean computeValue() {
                return value;
            }
        };
    }

    @Override
    public ObservableBooleanValue authorizedProperty(ObservableValue authorityProperty) {
        return null; // Not yet implemented
    }
}
