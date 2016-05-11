package naga.core.spi.platform.client.teavm;

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
final class JSUtil {

    @JSBody(params = {}, script = "return {};")
    public static native JSObject newJSObject();

    @JSBody(params = {}, script = "return [];")
    public static native JSObject newJSArray();

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
    public static native boolean isNull(JSObject value);

    @JSBody(params = "object", script = "return typeof object === 'undefined';")
    public static native boolean isUndefined(JSObject object);

    @JSBody(params = "object", script = "return typeof object;")
    public static native String getJsType(JSObject object);

    @JSBody(params = "object", script = "return Object.prototype.toString.apply(object) === '[object Array]';")
    public static native boolean isArray(JSObject object);

    @JSBody(params = {"obj1", "obj2"}, script = "return obj1 === obj2;")
    public static native boolean areEquals(JSObject obj1, JSObject obj2);

    @JSBody(params = "object", script = "return object ? object : null")
    public static native JSObject nullOr(JSObject object);

    public static JSObject parse(String jsonString) {
        return JSON.parse(jsonString);
    }

    public static String stringify(JSObject jso) {
        return JSON.stringify(jso);
    }

    public static JSObject copy(JSObject jso) {
        return parse(stringify(jso));
    }

    public static JSObject j2js(Object value) {
        if (value == null)
            return null;
        if (value instanceof TeaVmJsonElement)
            return j2jsElement(value);
        return j2jsScalar(value);
    }

    public static JSObject j2jsElement(Object element) {
        if (element == null)
            return null;
        return ((TeaVmJsonElement) element).getJsValue();
    }

    public static JSObject j2jsScalar(Object value) {
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

    /*public static Object js2j(JSObject jsv) {
        if (jsv == null)
            return null;
        Object scalar = js2jScalar(jsv);
        if (scalar != null)
            return scalar;
        return js2Element(jsv);
    }

    public static TeaVmJsonElement js2Element(JSObject jsv) {
        if (isArray(jsv))
            return new TeaVmJsonArray((JSArray) jsv);
        if (isObject(jsv))
            return new TeaVmJsonObject(jsv);
        return (TeaVmJsonElement) jsv;
    }*/

    public static <T> T js2jScalar(JSObject jsv) {
        if (JSString.isInstance(jsv))
            return (T) ((JSString) jsv).stringValue();
        if (isNumber(jsv))
            return (T) js2Number((JSNumber) jsv);
        if (isBoolean(jsv))
            return (T) (Object) ((JSBoolean) jsv).booleanValue();
        return (T) jsv;
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

}
