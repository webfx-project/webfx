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

}
