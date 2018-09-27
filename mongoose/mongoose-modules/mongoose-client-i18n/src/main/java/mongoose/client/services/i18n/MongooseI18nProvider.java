package mongoose.client.services.i18n;

import webfx.framework.services.i18n.spi.impl.I18nResourceBasedProvider;

/**
 * @author Bruno Salmon
 */
public final class MongooseI18nProvider extends I18nResourceBasedProvider {

    public MongooseI18nProvider() {
        super("mongoose/client/services/i18n/dictionaries/{lang}.json");
    }
}
