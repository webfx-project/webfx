package mongoose.client.services.i18n;


import webfx.framework.client.services.i18n.spi.impl.json.JsonI18nProvider;

/**
 * @author Bruno Salmon
 */
public final class MongooseI18nProvider extends JsonI18nProvider {

    public MongooseI18nProvider() {
        super("mongoose/client/services/i18n/dictionaries/{lang}.json");
    }
}
