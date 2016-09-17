package naga.framework.ui.i18n.impl;

import naga.commons.util.Strings;
import naga.platform.json.Json;
import naga.platform.json.spi.JsonObject;

/**
 * @author Bruno Salmon
 */
public class JsonDictionary implements Dictionary {

    private final JsonObject json;

    public JsonDictionary(JsonObject json) {
        this.json = json;
    }

    public JsonDictionary(String json) {
        this(Json.parseObject(json));
    }

    @Override
    public String getMessage(Object key) {
        return json.getString(Strings.toString(key));
    }
}
