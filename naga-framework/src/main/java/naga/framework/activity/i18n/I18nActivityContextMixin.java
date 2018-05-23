package naga.framework.activity.i18n;

import naga.framework.services.i18n.spi.I18nProvider;
import naga.platform.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface I18nActivityContextMixin
        <C extends I18nActivityContext<C>>

        extends ActivityContextMixin<C>,
        I18nActivityContext<C> {

    @Override
    default I18nProvider getI18n() {
        return getActivityContext().getI18n();
    }

    @Override
    default void setI18n(I18nProvider i18n) {
        getActivityContext().setI18n(i18n);
    }
}
