package naga.platform.services.json.parser;

import naga.platform.services.json.WritableJsonArray;
import naga.platform.services.json.WritableJsonObject;
import naga.platform.services.json.parser.javacup.JavaCupJsonParser;
import naga.platform.services.json.parser.jflex.JsonLexer;

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
