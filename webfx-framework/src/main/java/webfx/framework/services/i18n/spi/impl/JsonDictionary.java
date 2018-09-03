package webfx.framework.services.i18n.spi.impl;

import webfx.platforms.core.util.Strings;
import webfx.framework.services.i18n.Dictionary;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.json.JsonObject;

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
    public String getMessage(Object key) {
        return json.getString(Strings.toString(key));
    }
}
