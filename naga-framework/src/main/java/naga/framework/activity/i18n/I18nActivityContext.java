package naga.framework.activity.i18n;

import naga.framework.services.i18n.spi.HasI18nProvider;
import naga.framework.services.i18n.spi.I18nProvider;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface I18nActivityContext
        <THIS extends I18nActivityContext<THIS>>

        extends ActivityContext<THIS>,
        HasI18nProvider {

    void setI18n(I18nProvider i18n);
}
