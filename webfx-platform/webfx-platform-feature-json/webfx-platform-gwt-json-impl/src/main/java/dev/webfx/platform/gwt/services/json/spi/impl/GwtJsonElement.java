/*
 * Note: this code is a fork of Goodow realtime-json project https://github.com/goodow/realtime-json
 */

/*
 * Copyright 2013 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package dev.webfx.platform.gwt.services.json.spi.impl;

import com.google.gwt.core.client.JavaScriptObject;
import dev.webfx.platform.shared.services.json.ElementType;
import dev.webfx.platform.shared.services.json.WritableJsonElement;
import dev.webfx.platform.shared.services.json.parser.BuiltInJsonParser;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the webfx project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonElement.java">Original Goodow class</a>
 */
abstract class GwtJsonElement extends JavaScriptObject implements WritableJsonElement {

    // GWT: Constructors must be 'protected' in subclasses of JavaScriptObject
    protected GwtJsonElement() {} // instances are actually always obtained from a javascript cast

    @Override
    public final GwtJsonElement getNativeElement() {
        return cast();
    }

    @Override
    public final ElementType getNativeElementType(Object nativeElement) {
        if (nativeElement == null)
            return ElementType.NULL;
        switch(typeof(nativeElement)) {
            case "object":  return isArray(nativeElement) ? ElementType.ARRAY : ElementType.OBJECT;
            case "string":  return ElementType.STRING;
            case "number":  return ElementType.NUMBER;
            case "boolean": return ElementType.BOOLEAN;
            default:        return ElementType.UNDEFINED;
        }
    }

    private static native String typeof(Object obj) /*-{
        return typeof obj;
    }-*/;

    private static native boolean isArray(Object obj) /*-{
        // ensure that array detection works cross-frame
        return Object.prototype.toString.apply(obj) === '[object Array]';
    }-*/;

    @Override
    public final int size() {
        return isObject() ? sizeObject() : sizeArray();
    }

    private final native int sizeArray() /*-{
        return this.length;
    }-*/;

    /**
     * Returns the size of the map (the number of keys).
     * <p>
     * <p>NB: This method is currently O(N) because it iterates over all keys.
     *
     * @return the size of the map.
     */
    private final native int sizeObject() /*-{
        size = 0;
        for (key in this)
          if (Object.prototype.hasOwnProperty.call(this, key))
            size++;
        return size;
    }-*/;

    @Override
    public final void clear() {
        if (isObject())
            clearObject();
        else
            clearArray();
    }

    private native GwtJsonArray clearArray() /*-{
        this.length = 0;
        return this;
    }-*/;

    private native GwtJsonObject clearObject() /*-{
        for (var key in this)
            if (Object.prototype.hasOwnProperty.call(this, key))
                delete this[key];
        return this;
    }-*/;

    @Override
    public final GwtJsonObject createNativeObject() {
        return GwtJsonObject.create();
    }

    @Override
    public final GwtJsonArray createNativeArray() {
        return GwtJsonArray.create();
    }


    @Override
    public final native GwtJsonArray nativeToJavaJsonArray(Object nativeArray) /*-{
        return nativeArray;
    }-*/;

    @Override
    public final native GwtJsonObject nativeToJavaJsonObject(Object nativeObject) /*-{
        return nativeObject;
    }-*/;

    @Override
    public final native <T> T nativeToJavaScalar(Object nativeScalar) /*-{
        return nativeScalar;
    }-*/;

    @Override
    public final Object anyJavaToNative(Object value) {
        return javaToNativeScalar(value);
    }

    @Override
    public final Object javaToNativeScalar(Object value) {
        if (value == null)
            return null;
        if (value instanceof Boolean)
            return toJsBoolean((Boolean) value);
        if (value instanceof Number)
            return toJsDouble(((Number) value).doubleValue());
        return value;
    }

    private static native JavaScriptObject toJsBoolean(boolean value) /*-{
        return value;
    }-*/;

    private static native JavaScriptObject toJsDouble(double value) /*-{
        return value;
    }-*/;

    @Override
    public final Object parseNativeObject(String text) {
        if (fastCheckStrictJson(text))
            try {
                return callBrowserJsonParser(text); // Faster but strict parser (ex: {"key": value})
            } catch (Exception e) { // Probably a json with non-strict keys (ex: {key: value}) -> rejected by the browser parser
            }
        return BuiltInJsonParser.parseJsonObject(text); // Re-trying with the built-in parser (slower but non-strict)
    }

    private boolean fastCheckStrictJson(String text) {
        // Checking if first json object looks strict or not
        int p = text.indexOf('{') + 1;
        if (p == 0)
            return true;
        for (int n = text.length(); p < n; p++) {
            char c = text.charAt(p);
            if (!Character.isWhitespace(c))
                return c == '"';
        }
        return false;
    }

    @Override
    public final Object parseNativeArray(String text) {
        if (fastCheckStrictJson(text))
            try {
                return callBrowserJsonParser(text); // Faster but strict parser
            } catch (Exception e) { // Probably a json with non-strict keys -> rejected by the browser parser
            }
        return BuiltInJsonParser.parseJsonArray(text); // Re-trying with the built-in parser (slower but non-strict)
    }

    private static native JavaScriptObject callBrowserJsonParser(String text) /*-{
        return JSON.parse(text);
    }-*/;

}
