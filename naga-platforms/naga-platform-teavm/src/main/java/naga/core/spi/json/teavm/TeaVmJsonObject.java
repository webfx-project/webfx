package naga.core.spi.json.teavm;

import naga.core.spi.json.JsonObject;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;

import java.util.Collection;

/**
 * Client-side implementation of JsonObject interface.
 *
 * @author Bruno Salmon
 */
public final class TeaVmJsonObject extends TeaVmJsonElement implements JsonObject {

    public static TeaVmJsonObject create(JSObject jso) {
        if (jso == null || JSUtil.isUndefined(jso))
            return null;
        return new TeaVmJsonObject(jso);
    }

    public TeaVmJsonObject() {
        this(JSUtil.newJSObject());
    }

    public TeaVmJsonObject(JSObject jso) {
        super(jso);
    }


    @Override
    public JSObject getRaw(String key) {
        return JSUtil.getJSValue(jsValue, key);
    }

    @Override
    public double getDouble(String key) {
        return JSUtil.js2Double(getRaw(key));
    }


    @Override
    public String getString(String key) {
        return JSUtil.js2String(getRaw(key));
    }

    @Override
    public native boolean has(String key) /*-{
        return key in this;
    }-*/;

    @Override
    public native Collection keys(); /* {
        JSArray jsArray = JSUtil.getKeys(jsValue);
        if (jsArray == null || JSUtil.isUndefined(jsArray))
            return null;
        return new TeaVmJsonArray(jsArray);
    }*/

    @Override
    public <T> T remove(String key) {
        JSUtil.deleteJSValue(jsValue, key);
        return null;
    } /*-{
        toRtn = this[key];
        delete this[key];
        return toRtn;
    }-*/;

    @Override
    public void setRaw(String key, Object value) {
        JSUtil.setJSValue(jsValue, key, (JSObject) value);
    }

    @Override
    public void set(String key, boolean bool) {
        setRaw(key, JSBoolean.valueOf(bool));
    }

    @Override
    public void set(String key, double number) {
        setRaw(key, JSNumber.valueOf(number));
    }

    @Override
    public void set(String key, Object element) {
        setRaw(key, JSUtil.j2js(element));
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

    @Override
    public native Collection values();
}