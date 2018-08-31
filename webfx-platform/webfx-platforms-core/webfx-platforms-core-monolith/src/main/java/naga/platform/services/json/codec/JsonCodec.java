package naga.platform.services.json.codec;

import naga.platform.services.json.JsonObject;
import naga.platform.services.json.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecId();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
