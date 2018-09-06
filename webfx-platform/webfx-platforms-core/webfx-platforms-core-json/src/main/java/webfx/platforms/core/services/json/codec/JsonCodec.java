package webfx.platforms.core.services.json.codec;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecId();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
