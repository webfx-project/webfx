package naga.core.jsoncodec;

import naga.core.spi.json.JsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecID();

    void encodeToJson(T javaObject, JsonObject json);

    T decodeFromJson(JsonObject json);

}
