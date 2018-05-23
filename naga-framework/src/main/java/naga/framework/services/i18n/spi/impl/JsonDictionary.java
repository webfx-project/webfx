package naga.framework.services.i18n.spi.impl;

import naga.util.Strings;
import naga.framework.services.i18n.Dictionary;
import naga.platform.services.json.Json;
import naga.platform.services.json.JsonObject;

/**
 * @author Bruno Salmon
 */
class JsonDictionary implements Dictionary {

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
