package naga.platform.services.json.codec;

/*
 * @author Bruno Salmon
 */

import naga.platform.services.json.WritableJsonObject;

public abstract class AbstractJsonCodec<T> implements JsonCodec<T> {

    private final String codecId;

    public AbstractJsonCodec(Class<? extends T> javaClass, String codecId) {
        this.codecId = codecId;
        JsonCodecManager.registerJsonCodec(javaClass, this);
    }

    @Override
    public String getCodecId() {
        return codecId;
    }

    protected static void encodeKey(String key, Object value, WritableJsonObject json) {
        json.set(key, JsonCodecManager.encodeToJson(value));
    }

    protected static void encodeKeyIfNotNull(String key, Object value, WritableJsonObject json) {
        if (value != null)
            encodeKey(key, value, json);
    }

}
