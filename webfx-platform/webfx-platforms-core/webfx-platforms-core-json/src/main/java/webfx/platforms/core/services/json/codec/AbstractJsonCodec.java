package webfx.platforms.core.services.json.codec;

/*
 * @author Bruno Salmon
 */

import webfx.platforms.core.services.json.WritableJsonObject;

public abstract class AbstractJsonCodec<T> implements JsonCodec<T> {

    private final Class<? extends T> javaClass;
    private final String codecId;

    public AbstractJsonCodec(Class<? extends T> javaClass, String codecId) {
        this.javaClass = javaClass;
        this.codecId = codecId;
    }

    @Override
    public Class<? extends T> getJavaClass() {
        return javaClass;
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
