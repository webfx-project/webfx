package naga.framework.services.i18n.spi;

import naga.framework.services.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public interface HasI18nProvider {

    default I18nProvider getI18n() {
        return I18n.getProvider();
    }

}
