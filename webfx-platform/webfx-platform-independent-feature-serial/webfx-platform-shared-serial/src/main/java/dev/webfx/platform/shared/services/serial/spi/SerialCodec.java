package dev.webfx.platform.shared.services.serial.spi;

import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface SerialCodec<T> {

    String getCodecId();

    Class<? extends T> getJavaClass();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
