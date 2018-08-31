package webfx.platform.services.json.parser;

import webfx.platform.services.json.WritableJsonArray;
import webfx.platform.services.json.WritableJsonObject;
import webfx.platform.services.json.parser.javacup.JavaCupJsonParser;
import webfx.platform.services.json.parser.jflex.JsonLexer;

import java.io.StringReader;

/**
 * @author Bruno Salmon
 */
public class BuiltInJsonParser {

    public static WritableJsonObject parseJsonObject(String json) {
        return parseWithJavaCup(json);
    }

    public static WritableJsonArray parseJsonArray(String json) {
        return parseWithJavaCup(json);
    }

    private static <T> T parseWithJavaCup(String json) {
        try {
            return (T) new JavaCupJsonParser(new JsonLexer(new StringReader(json))).parse().value;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

}
