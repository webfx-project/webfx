package naga.core.spi.platform.client.cn1;

import com.codename1.io.JSONParser;
import naga.core.json.WritableJsonArray;
import naga.core.json.WritableJsonObject;
import naga.core.json.listmap.ListMapJsonElement;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
interface Cn1JsonElement extends ListMapJsonElement {

    JSONParser cn1Parser = new JSONParser();

    @Override
    default Object parseNativeObject(String text) {
        try {
            return cn1Parser.parseJSON(new StringReader(text));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    default Object parseNativeArray(String text) {
        return null;
    }

    default WritableJsonObject nativeToJavaJsonObject(Object nativeObject) {
        if (nativeObject == null || nativeObject instanceof WritableJsonObject)
            return (WritableJsonObject) nativeObject;
        return new Cn1JsonObject((Map) nativeObject);
    }

    default WritableJsonArray nativeToJavaJsonArray(Object nativeArray) {
        if (nativeArray == null || nativeArray instanceof WritableJsonArray)
            return (WritableJsonArray) nativeArray;
        return new Cn1JsonArray((List) nativeArray);
    }

}