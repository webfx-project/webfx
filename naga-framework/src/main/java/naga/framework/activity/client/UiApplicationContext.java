package naga.framework.activity.client;

import javafx.beans.property.Property;
import naga.framework.ui.i18n.I18n;
import naga.platform.activity.client.ApplicationContext;

/**
 * @author Bruno Salmon
 */
public interface UiApplicationContext
        <THIS extends UiApplicationContext<THIS>>

        extends UiActivityContext<THIS>,
        ApplicationContext<THIS> {

    Property<Boolean> windowBoundProperty();

    default boolean isWindowBound() { return windowBoundProperty().getValue(); }

    THIS setI18n(I18n i18n);

    static UiApplicationContextFinal create(String[] mainArgs) {
        return new UiApplicationContextFinal(mainArgs, UiActivityContext::create);
    }

    static <C extends UiApplicationContext<C>> C getUiApplicationContext() {
        return (C) ApplicationContext.get();
    }

}
