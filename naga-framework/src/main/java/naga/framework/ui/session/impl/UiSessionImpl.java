package naga.framework.ui.session.impl;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import naga.framework.ui.session.UiSession;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.util.Objects;


/**
 * @author Bruno Salmon
 */
public class UiSessionImpl implements UiSession {

    private final Property<Object> userPrincipalProperty = new SimpleObjectProperty<>();
    private final ObservableBooleanValue loggedInProperty = BooleanExpression.booleanExpression(Properties.compute(userPrincipalProperty, Objects::nonNull));

    @Override
    public Property<Object> userPrincipalProperty() {
        return userPrincipalProperty;
    }

    @Override
    public ObservableBooleanValue loggedInProperty() {
        return loggedInProperty;
    }

    @Override
    public ObservableBooleanValue authorizedProperty(Object operationAuthorizationRequest) {
        return new BooleanBinding() {
            Object userPrincipal;
            boolean value;
            { bind(userPrincipalProperty()); }

            @Override
            protected void onInvalidating() {
                if (userPrincipal != getUserPrincipal()) {
                    value = false;
                    isAuthorized(operationAuthorizationRequest).setHandler(ar -> {
                        userPrincipal = getUserPrincipal();
                        if (ar.succeeded())
                            Toolkit.get().scheduler().runInUiThread(() -> {
                                value = ar.result();
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
    public ObservableBooleanValue authorizedProperty(ObservableValue operationAuthorizationRequestProperty) {
        return null; // Not yet implemented
    }
}
