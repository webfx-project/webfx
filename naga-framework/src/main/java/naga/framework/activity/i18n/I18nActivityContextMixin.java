package naga.framework.activity.i18n;

import naga.framework.ui.i18n.I18n;
import naga.platform.activity.ActivityContextMixin;

/**
 * @author Bruno Salmon
 */
public interface I18nActivityContextMixin
        <C extends I18nActivityContext<C>>

        extends ActivityContextMixin<C>,
        I18nActivityContext<C> {

    @Override
    default I18n getI18n() {
        return getActivityContext().getI18n();
    }

    @Override
    default void setI18n(I18n i18n) {
        getActivityContext().setI18n(i18n);
    }
}
