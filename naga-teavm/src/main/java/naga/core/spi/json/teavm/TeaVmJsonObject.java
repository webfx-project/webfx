package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;

/**
 * Client-side implementation of JsonObject interface.
 *
 * @author Bruno Salmon
 */
final class TeaVmJsonObject extends TeaVmJsonElement implements JsonObject {

    public static TeaVmJsonObject create() {
        return new TeaVmJsonObject();
    }

    public static TeaVmJsonObject create(JSObject jso) {
        return new TeaVmJsonObject(jso);
    }

    public TeaVmJsonObject() {
        this(JSUtil.newJSObject());
    }

    public TeaVmJsonObject(JSObject jso) {
        super(jso);
    }

    private JSObject getJsValue(String key) {
        return JSUtil.getJSValue(jsValue, key);
    }

    @Override
    public native <T> void forEach(MapIterator<T> handler) /*-{
    for (key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        handler.
        @com.goodow.realtime.json.JsonObject.MapIterator::call(Ljava/lang/String;Ljava/lang/Object;)
        (key, this[key]);
      }
    }
  }-*/;


    @Override
    public TeaVmJsonElement get(String key) {
        return JSUtil.js2Element(getJsValue(key));
    }

    @Override
    public native TeaVmJsonArray getArray(String key);

    @Override
    public native boolean getBoolean(String key);

    @Override
    public double getNumber(String key) {
        return JSUtil.js2Double(getJsValue(key));
    }

    @Override
    public native TeaVmJsonObject getObject(String key);

    @Override
    public String getString(String key) {
        return JSUtil.js2String(getJsValue(key));
    }

    @Override
    public JsonType getType(String key) {
        return JSUtil.getType(getJsValue(key));
    }

    @Override
    public native boolean has(String key) /*-{
    return key in this;
  }-*/;

    @Override
    public JsonArray keys() {
        throw new UnsupportedOperationException();
    }

    @Override
    public native <T> T remove(String key) /*-{
    toRtn = this[key];
    delete this[key];
    return toRtn;
  }-*/;

    @Override
    public JsonObject set(String key, boolean bool) {
        JSUtil.setJSValue(jsValue, key, JSBoolean.valueOf(bool));
        return this;
    }

    @Override
    public JsonObject set(String key, double number) {
        JSUtil.setJSValue(jsValue, key, JSNumber.valueOf(number));
        return this;
    }

    @Override
    public JsonObject set(String key, Object element) {
        JSUtil.setJSValue(jsValue, key, JSUtil.j2js(element));
        return this;
    }

    /**
     * Returns the size of the map (the number of keys).
     * <p>
     * <p>NB: This method is currently O(N) because it iterates over all keys.
     *
     * @return the size of the map.
     */
    @Override
    public final native int size() /*-{
    size = 0;
    for (key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        size++;
      }
    }
    return size;
  }-*/;

}