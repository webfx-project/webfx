package webfx.framework.services.i18n.spi.impl;

import webfx.util.Strings;
import webfx.framework.services.i18n.Dictionary;
import webfx.platform.services.json.Json;
import webfx.platform.services.json.JsonObject;

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
