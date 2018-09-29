package webfx.framework.ui.uirouter.uisession.impl;

import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import webfx.framework.ui.uirouter.uisession.UiSession;
import webfx.fxkits.core.util.properties.Properties;
import webfx.platforms.core.util.Objects;


/**
 * @author Bruno Salmon
 */
public final class UiSessionImpl implements UiSession {

    private final Property<Object> userPrincipalProperty = new SimpleObjectProperty<>();
    private final ObservableBooleanValue loggedInProperty = BooleanExpression.booleanExpression(Properties.compute(userPrincipalProperty, Objects::nonNull));

    private final static UiSession INSTANCE = new UiSessionImpl();

    public static UiSession getInstance() {
        return INSTANCE;
    }

    private UiSessionImpl() {
    }

    @Override
    public Property<Object> userPrincipalProperty() {
        return userPrincipalProperty;
    }

    @Override
    public ObservableBooleanValue loggedInProperty() {
        return loggedInProperty;
    }

}
