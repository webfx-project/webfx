package dev.webfx.platform.teavm.services.json.spi.impl;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;
import dev.webfx.platform.shared.services.json.ElementType;
import dev.webfx.platform.shared.services.json.WritableJsonElement;

/*
 * @author Bruno Salmon
 */
abstract class TeaVmJsonElement implements WritableJsonElement {

    protected final JSObject nativeElement; // As opposed to GWT, TeaVM needs the element to be embed as a member field

    TeaVmJsonElement(JSObject nativeElement) {
        this.nativeElement = nativeElement;
    }

    @Override
    public JSObject getNativeElement() {
        return nativeElement;
    }

    @Override
    public ElementType getNativeElementType(Object nativeElement) {
        if (nativeElement == null)
            return ElementType.NULL;
        JSObject jso = (JSObject) nativeElement;
        switch(typeof(jso)) {
            case "object":  return isArray(jso) ? ElementType.ARRAY : ElementType.OBJECT;
            case "string":  return ElementType.STRING;
            case "number":  return ElementType.NUMBER;
            case "boolean": return ElementType.BOOLEAN;
            default:        return ElementType.UNDEFINED;
        }
    }

    @JSBody(params = "object", script = "return typeof object;")
    static native String typeof(JSObject object);

    @JSBody(params = "object", script = "return Object.prototype.toString.apply(object) === '[object Array]';")
    static native boolean isArray(JSObject object);

    @Override
    public final void clear() {
        if (isObject())
            clearObject();
        else
            clearArray();
    }

    private native void clearArray() /*-{
        this.length = 0;
    }-*/;

    private native void clearObject() /*-{
        for (var key in this) {
          if (Object.prototype.hasOwnProperty.call(this, key)) {
            delete this[key];
          }
        }
    }-*/;

    @Override
    public JSObject createNativeObject() {
        return newJSObject();
    }

    @Override
    public JSArray createNativeArray() {
        return newJSArray();
    }


    @Override
    public TeaVmJsonArray nativeToJavaJsonArray(Object nativeArray) {
        if (nativeArray instanceof TeaVmJsonArray)
            return (TeaVmJsonArray) nativeArray;
        return TeaVmJsonArray.create((JSArray) nativeArray);
    }

    @Override
    public TeaVmJsonObject nativeToJavaJsonObject(Object nativeObject) {
        if (nativeObject instanceof TeaVmJsonObject)
            return (TeaVmJsonObject) nativeObject;
        return TeaVmJsonObject.create((JSObject) nativeObject);
    }

    @Override
    public <T> T nativeToJavaScalar(Object nativeScalar) {
        switch (getNativeElementType(nativeScalar)) {
            case STRING:  return (T) ((JSString) nativeScalar).stringValue();
            case BOOLEAN: return (T) (Object) ((JSBoolean) nativeScalar).booleanValue();
            case NUMBER:  return (T) js2Number((JSNumber) nativeScalar);
        }
        return (T) nativeScalar;
    }

    @Override
    public JSObject javaToNativeScalar(Object scalar) {
        if (scalar == null)
            return null;
        if (scalar instanceof String)
            return JSString.valueOf((String) scalar);
        if (scalar instanceof Boolean)
            return JSBoolean.valueOf((Boolean) scalar);
        if (scalar instanceof Float)
            return JSNumber.valueOf((Float) scalar);
        if (scalar instanceof Double)
            return JSNumber.valueOf((Double) scalar);
        if (scalar instanceof Number)
            return JSNumber.valueOf(((Number) scalar).intValue());
        if (scalar instanceof Character)
            return JSString.valueOf(String.valueOf(scalar));
        return (JSObject) scalar;
    }

    @Override
    public String toString() {
        return toJsonString();
    }


    /** Static JS utility methods **/

    @JSBody(params = {}, script = "return {};")
    static native JSObject newJSObject();

    @JSBody(params = {}, script = "return [];")
    static native JSArray newJSArray();

    @JSBody(params = {"jso", "key", "value"}, script = "jso[key] = value;")
    static native void setJSValue(JSObject jso, String key, JSObject value);

    @JSBody(params = {"jso", "key"}, script = "return jso[key] || null;")
    static native JSObject getJSValue(JSObject jso, String key);

    @JSBody(params = {"jso", "key"}, script = "delete jso[key];")
    static native JSObject deleteJSValue(JSObject jso, String key);

    @JSBody(params = {"jso"}, script = "" +
            "    var keys = [];\n" +
            "    for(var key in jso) {\n" +
            "      if (Object.prototype.hasOwnProperty.call(jso, key) && key != '$H') {\n" +
            "        keys.push(key);\n" +
            "      }\n" +
            "    }\n" +
            "    return keys;")
    static native JSArray<JSString> getKeys(JSObject jso);

    @JSBody(params = "object", script = "return typeof object === 'undefined';")
    static native boolean isUndefined(JSObject object);


    static Number js2Number(JSNumber jsn) {
        // No distinction between numbers in javascript
        double d = jsn.doubleValue(); // So we convert into a java double by default
        if (d != Math.floor(d)) // if it has decimals (not a round integer value)
            return d; // we return it as is
        // Casting it as long or it depending on the value
        if (Math.abs(d) > Integer.MAX_VALUE) // If grater than max integer value
            return (long) d; // we cast it as a long
        return (int) d; // otherwise as an int
    }

    static String js2String(JSObject jsv) {
        return jsv == null || isUndefined(jsv) ? null : ((JSString) jsv).stringValue();
    }

    static double js2Double(JSObject jsv) {
        return jsv == null || isUndefined(jsv) ? 0 : ((JSNumber) jsv).doubleValue();
    }

}
