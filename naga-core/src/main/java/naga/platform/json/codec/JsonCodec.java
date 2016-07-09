package naga.platform.json.codec;

import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecId();

    void encodeToJson(T javaObject, WritableJsonObject json);

    T decodeFromJson(JsonObject json);

}
