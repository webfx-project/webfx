package webfx.platform.services.json.codec;

import webfx.platform.services.json.JsonObject;
import webfx.platform.services.json.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecId();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
