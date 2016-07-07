package naga.commons.json.codec;

import naga.commons.json.spi.JsonObject;
import naga.commons.json.spi.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecId();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
