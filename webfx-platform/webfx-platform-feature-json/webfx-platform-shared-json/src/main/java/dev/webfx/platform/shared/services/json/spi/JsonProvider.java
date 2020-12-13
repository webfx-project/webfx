package dev.webfx.platform.shared.services.json.spi;

import dev.webfx.platform.shared.services.json.JsonFormatter;
import dev.webfx.platform.shared.services.json.JsonParser;
import dev.webfx.platform.shared.services.json.WritableJsonArray;
import dev.webfx.platform.shared.services.json.WritableJsonObject;

/**
 * @author Bruno Salmon
 */
public interface JsonProvider extends JsonParser, JsonFormatter {

    /**
     * Create an empty native object.
     * @return a new empty native object
     */
    Object createNativeObject();

    /**
     * Create an empty native array.
     * @return a new empty native array
     */
    Object createNativeArray();

    /**
     * Create an empty json object.
     * @return a new empty json object
     */
    default WritableJsonObject createJsonObject() {
        return nativeToJavaJsonObject(createNativeObject());
    }

    /**
     * Create an empty json array.
     * @return a new empty json array
     */
    default WritableJsonArray createJsonArray() {
        return nativeToJavaJsonArray(createNativeArray());
    }

}
