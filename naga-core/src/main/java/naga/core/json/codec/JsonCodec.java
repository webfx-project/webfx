package naga.core.json.codec;

import naga.core.json.JsonObject;
import naga.core.json.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecId();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
