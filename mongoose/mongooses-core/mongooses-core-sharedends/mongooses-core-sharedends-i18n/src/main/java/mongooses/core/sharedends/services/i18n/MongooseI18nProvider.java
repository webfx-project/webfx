package mongooses.core.sharedends.services.i18n;

import webfx.framework.services.i18n.spi.impl.I18nResourceBasedProvider;

/**
 * @author Bruno Salmon
 */
public final class MongooseI18nProvider extends I18nResourceBasedProvider {

    public MongooseI18nProvider() {
        super("mongooses/core/sharedends/i18n/dictionaries/{lang}.json");
    }
}
