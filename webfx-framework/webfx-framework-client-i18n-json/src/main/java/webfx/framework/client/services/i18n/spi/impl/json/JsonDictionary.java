package webfx.framework.client.services.i18n.spi.impl.json;

import webfx.framework.client.services.i18n.TranslationPart;
import webfx.platform.shared.util.Strings;
import webfx.framework.client.services.i18n.Dictionary;
import webfx.platform.shared.services.json.Json;
import webfx.platform.shared.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
final class JsonDictionary implements Dictionary {

    private final JsonObject json;

    JsonDictionary(JsonObject json) {
        this.json = json;
    }

    JsonDictionary(String json) {
        this(Json.parseObject(json));
    }

    @Override
    public String getPartTranslation(Object i18nKey, TranslationPart part) {
        Object o = json.get(Strings.toString(i18nKey));
        if (o instanceof JsonObject)
            return ((JsonObject) o).getString(part.name().toLowerCase());
        return part == TranslationPart.TEXT ? Strings.toString(o) : null;
    }
}
