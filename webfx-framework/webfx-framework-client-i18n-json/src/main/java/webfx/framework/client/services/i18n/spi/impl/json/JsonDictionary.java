package webfx.framework.client.services.i18n.spi.impl.json;

import webfx.framework.client.services.i18n.I18nPart;
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
    public String getI18nPartValue(Object i18nKey, I18nPart part) {
        String jsonKey = Strings.toString(i18nKey);
        Object o = json.get(jsonKey);
        if (o instanceof JsonObject)
            return ((JsonObject) o).getString(part.name().toLowerCase());
        return part == I18nPart.TEXT ? Strings.toString(o) : null;
    }
}
