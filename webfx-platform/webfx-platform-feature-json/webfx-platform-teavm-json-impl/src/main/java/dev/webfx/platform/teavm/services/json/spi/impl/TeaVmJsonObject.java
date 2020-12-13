package dev.webfx.platform.teavm.services.json.spi.impl;

import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSBoolean;
import org.teavm.jso.core.JSNumber;
import org.teavm.jso.core.JSString;
import dev.webfx.platform.shared.services.json.WritableJsonObject;

/**
 * TeaVM implementation of JsonObject interface.
 *
 * @author Bruno Salmon
 */
public final class TeaVmJsonObject extends TeaVmJsonElement implements WritableJsonObject {

    public static TeaVmJsonObject create(JSObject jso) {
        if (jso == null || isUndefined(jso))
            return null;
        return new TeaVmJsonObject(jso);
    }

    public TeaVmJsonObject() {
        this(newJSObject());
    }

    TeaVmJsonObject(JSObject jso) {
        super(jso);
    }


    @Override
    public JSObject getNativeElement(String key) {
        return getJSValue(nativeElement, key);
    }

    @Override
    public Double getDouble(String key) {
        return js2Double(getNativeElement(key));
    }


    @Override
    public String getString(String key) {
        return js2String(getNativeElement(key));
    }

    @Override
    public native boolean has(String key) /*-{
        return key in this;
    }-*/;

    @Override
    public TeaVmJsonArray keys() {
        return TeaVmJsonArray.create(getKeys(nativeElement));
    }

    @Override
    public <T> T remove(String key) {
        deleteJSValue(nativeElement, key);
        return null;
    }

    @Override
    public TeaVmJsonObject setNativeElement(String key, Object element) {
        setJSValue(nativeElement, key, (JSObject) element);
        return this;
    }

    @Override
    public TeaVmJsonObject set(String key, Boolean value) {
        return setNativeElement(key, JSBoolean.valueOf(value));
    }

    @Override
    public TeaVmJsonObject set(String key, Double value) {
        return setNativeElement(key, JSNumber.valueOf(value));
    }

    @Override
    public TeaVmJsonObject set(String key, String value) {
        return setNativeElement(key, JSString.valueOf(value));
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