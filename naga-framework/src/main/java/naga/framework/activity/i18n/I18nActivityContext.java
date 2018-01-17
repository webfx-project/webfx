package naga.framework.activity.i18n;

import naga.framework.ui.i18n.HasI18n;
import naga.framework.ui.i18n.I18n;
import naga.platform.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public interface I18nActivityContext
        <THIS extends I18nActivityContext<THIS>>

        extends ActivityContext<THIS>,
        HasI18n {

    void setI18n(I18n i18n);
}
