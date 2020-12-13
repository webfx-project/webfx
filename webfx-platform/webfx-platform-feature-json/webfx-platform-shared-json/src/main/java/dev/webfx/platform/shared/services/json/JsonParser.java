package dev.webfx.platform.shared.services.json;

import dev.webfx.platform.shared.services.json.parser.BuiltInJsonParser;

/**
 * @author Bruno Salmon
 */
public interface JsonParser extends JsonWrapper {

    /**
     * Parse a text into a native object.
     * @param text the text to parse
     * @return the native object
     */
    default Object parseNativeObject(String text) {
        return BuiltInJsonParser.parseJsonObject(text);
    }

    /**
     * Parse a text into a native array.
     * @param text the text to parse
     * @return the native array
     */
    default Object parseNativeArray(String text) {
        return BuiltInJsonParser.parseJsonArray(text);
    }

    /**
     * Parse a text into a json object.
     * @param text the text to parse
     * @return the json object
     */
    default WritableJsonObject parseObject(String text) {
        return nativeToJavaJsonObject(parseNativeObject(text));
    }

    /**
     * Parse a text into a json array.
     * @param text the text to parse
     * @return the json array
     */
    default WritableJsonArray parseArray(String text) {
        return nativeToJavaJsonArray(parseNativeArray(text));
    }
}
