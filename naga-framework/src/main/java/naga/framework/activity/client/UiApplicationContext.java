package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.framework.ui.i18n.I18n;
import naga.platform.activity.client.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface UiApplicationContext<C extends UiApplicationContext<C>> extends UiActivityContext<C>, ApplicationContext<C> {

    Property<Boolean> windowBoundProperty();

    default boolean isWindowBound() { return windowBoundProperty().getValue(); }

    C setI18n(I18n i18n);

    static UiApplicationContext create(String[] mainArgs) {
        return new UiApplicationContextImpl(mainArgs, UiActivityContext::create);
    }

    static <C extends UiApplicationContext<C>> C getUiApplicationContext() {
        return (C) ApplicationContext.get();
    }

}
