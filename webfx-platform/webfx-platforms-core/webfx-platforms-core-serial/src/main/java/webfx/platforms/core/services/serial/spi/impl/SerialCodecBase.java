package webfx.platforms.core.services.serial.spi.impl;

/*
 * @author Bruno Salmon
 */

import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.serial.spi.SerialCodec;
import webfx.platforms.core.services.serial.SerialCodecManager;

public abstract class SerialCodecBase<T> implements SerialCodec<T> {

    private final Class<? extends T> javaClass;
    private final String codecId;

    public SerialCodecBase(Class<? extends T> javaClass, String codecId) {
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
        json.set(key, SerialCodecManager.encodeToJson(value));
    }

    protected static void encodeKeyIfNotNull(String key, Object value, WritableJsonObject json) {
        if (value != null)
            encodeKey(key, value, json);
    }

}
