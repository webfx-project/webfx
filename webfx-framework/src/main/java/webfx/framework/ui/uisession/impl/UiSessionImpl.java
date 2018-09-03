package webfx.framework.ui.uisession.impl;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import webfx.framework.ui.uisession.UiSession;
import webfx.fxkits.core.properties.Properties;
import webfx.platforms.core.util.Objects;


/**
 * @author Bruno Salmon
 */
public final class UiSessionImpl implements UiSession {

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
