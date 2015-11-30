package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonException;
import naga.core.spi.json.JsonType;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;
import org.teavm.jso.json.JSON;

/**
 * @author Bruno Salmon
 */
public final class JSUtil {

    @JSBody(params = {}, script = "return {};")
    public static native JSObject newJSObject();

    @JSBody(params = {"jso", "key", "value"}, script = "jso[key] = value;")
    public static native void setJSValue(JSObject jso, String key, JSObject value);

    @JSBody(params = {"jso", "key"}, script = "return jso[key] || null;")
    public static native JSObject getJSValue(JSObject jso, String key);

    @JSBody(params = {"jso", "key"}, script = "delete jso[key];")
    public static native JSObject deleteJSValue(JSObject jso, String key);

    @JSBody(params = {"jso"}, script = "" +
            "    var keys = [];\n" +
            "    for(var key in this) {\n" +
            "      if (Object.prototype.hasOwnProperty.call(this, key) && key != '$H') {\n" +
            "        keys.push(key);\n" +
            "      }\n" +
            "    }\n" +
            "    return keys;")
    public static native JSArray<JSString> getKeys(JSObject jso);

    @JSBody(params = "value", script = "return value === null;")
    public static native boolean isNull(Object value);

    @JSBody(params = "object", script = "return typeof object === 'undefined';")
    public static native boolean isUndefined(JSObject object);

    @JSBody(params = "object", script = "return typeof object;")
    public static native String getJsType(Object object);

    @JSBody(params = "object", script = "return Object.prototype.toString.apply(object) === '[object Array]';")
    public static native boolean isArray(Object object);

    @JSBody(params = {"obj1", "obj2"}, script = "return obj1 === obj2;")
    public static native boolean areEquals(JSObject obj1, JSObject obj2);

    @JSBody(params = "object", script = "return object ? object : null")
    public static native JSObject nullOr(JSObject object);

    public static JSObject parse(String jsonString) throws JsonException {
        return JSON.parse(jsonString);
    }

    public static String stringify(JSObject jso) throws JsonException {
        return JSON.stringify(jso);
    }

    public static JSObject copy(JSObject jso) throws JsonException {
        return parse(stringify(jso));
    }

    public static JsonType getType(JSObject jso) {
        if (isNull(jso))
            return JsonType.NULL;
        String jsType = getJsType(jso);
        if ("string".equals(jsType))
            return JsonType.STRING;
        else if ("number".equals(jsType))
            return JsonType.NUMBER;
        else if ("boolean".equals(jsType))
            return JsonType.BOOLEAN;
        else if ("object".equals(jsType))
            return isArray(jso) ? JsonType.ARRAY : JsonType.OBJECT;
        assert false : "Unknown Json Type";
        return null;
    }

    public static JSObject j2js(Object value) {
        if (value == null)
            return null;
        if (value instanceof String)
            return JSString.valueOf((String) value);
        if (value instanceof Boolean)
            return JSBoolean.valueOf((Boolean) value);
        if (value instanceof Float)
            return JSNumber.valueOf((Float) value);
        if (value instanceof Double)
            return JSNumber.valueOf((Double) value);
        if (value instanceof Number)
            return JSNumber.valueOf(((Number) value).intValue());
        if (value instanceof Character)
            return JSString.valueOf(String.valueOf(value));
        return (JSObject) value;
    }

    public static Object js2j(JSObject jsv) {
        if (jsv == null)
            return null;
        if (JSString.isInstance(jsv))
            return ((JSString) jsv).stringValue();
        if (isNumber(jsv))
            return js2Number((JSNumber) jsv);
        if (isBoolean(jsv))
            return ((JSBoolean) jsv).booleanValue();
        if (isArray(jsv))
            return TeaVmJsonArray.create((JSArray) jsv);
        return TeaVmJsonObject.create(jsv);
    }

    @JSBody(params = "obj", script = "return typeof obj === 'boolean';")
    public static native boolean isBoolean(JSObject obj);

    @JSBody(params = "obj", script = "return typeof obj === 'number';")
    public static native boolean isNumber(JSObject obj);

    @JSBody(params = "obj", script = "return typeof obj === 'object';")
    public static native boolean isObject(JSObject obj);

    public static Number js2Number(JSNumber jsn) {
        // No distinction between numbers in javascript
        double d = jsn.doubleValue(); // So we convert into a java double by default
        if (d != Math.floor(d)) // if it has decimals (not a round integer value)
            return d; // we return it as is
        // Casting it as long or it depending on the value
        if (Math.abs(d) > Integer.MAX_VALUE) // If grater than max integer value
            return (long) d; // we cast it as a long
        return (int) d; // otherwise as an int
    }

    public static String js2String(JSObject jsv) {
        return jsv == null || isUndefined(jsv) ? null : ((JSString) jsv).stringValue();
    }

    public static Number js2Number(JSObject jsv) {
        return jsv == null || isUndefined(jsv) ? 0 : js2Number((JSNumber) jsv);
    }

    public static int js2Int(JSObject jsv) {
        return jsv == null || isUndefined(jsv) ? 0 : ((JSNumber) jsv).intValue();
    }

    public static double js2Double(JSObject jsv) {
        return jsv == null || isUndefined(jsv) ? 0 : ((JSNumber) jsv).doubleValue();
    }

    public static TeaVmJsonElement js2Element(JSObject jsv) {
        if (isArray(jsv))
            return TeaVmJsonArray.create((JSArray) jsv);
        return new TeaVmJsonObject(jsv);
    }

}
