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
package naga.core.spi.json.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import naga.core.json.ElementType;
import naga.core.json.WritableJsonElement;
import naga.core.util.Numbers;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonElement.java">Original Goodow class</a>
 */
abstract class GwtJsonElement extends GwtJsonValue implements WritableJsonElement {

    protected GwtJsonElement() {
    }

    @Override
    public final GwtJsonElement getNativeElement() {
        return cast();
    }

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
        return JavaScriptObject.createObject().cast();
    }

    @Override
    public final GwtJsonArray createNativeArray() {
        return JavaScriptObject.createArray().cast();
    }


    @Override
    public final native GwtJsonArray nativeToCompositeArray(Object nativeArray) /*-{
        return nativeArray;
    }-*/;

    @Override
    public final native GwtJsonObject nativeToCompositeObject(Object nativeObject) /*-{
        return nativeObject;
    }-*/;

    @Override
    public final native <T> T nativeToCompositeScalar(Object nativeScalar) /*-{
        return nativeScalar;
    }-*/;

    @Override
    public final Object anyCompositeToNative(Object value) {
        return compositeToNativeScalar(value);
    }

    @Override
    public final Object compositeToNativeScalar(Object value) {
        if (value == null)
            return null;
        if (value instanceof Boolean)
            return toJsBoolean((Boolean) value);
        if (Numbers.isNumber(value))
            return toJsDouble(Numbers.doubleValue(value));
        return value;
    }

    private static native JavaScriptObject toJsBoolean(boolean value) /*-{
        return value;
    }-*/;

    private static native JavaScriptObject toJsDouble(double value) /*-{
        return value;
    }-*/;

    @Override
    public final GwtJsonObject parseNativeObject(String text) {
        return parse0(text).cast();
    }

    @Override
    public final GwtJsonArray parseNativeArray(String text) {
        return parse0(text).cast();
    }

    private native static JavaScriptObject parse0(String jsonString) /*-{
        try { // First attempt without any transformation (only pure json will be accepted)
            return $wnd.JSON.parse(jsonString);
        } catch (e) { // Second attempt with transformation to allow relaxed json (single quotes strings and unquoted keys)
            // Converting single-quoted strings to double-quoted strings for Json compliance
            // Code borrowed from https://github.com/sindresorhus/to-double-quotes/blob/master/index.js
            jsonString = jsonString.replace(/(?:\\*)?'([^'\\]*\\.)*[^']*'/g, function (match) {
                return match
                    // unescape single-quotes
                    .replace(/\\'/g, '\'')
                    // escape escapes
                    .replace(/(^|[^\\])(\\+)"/g, '$1$2\\\"')
                    // escape double-quotes
                    .replace(/([^\\])"/g, '$1\\\"')
                    // convert
                    .replace(/^'|'$/g, '"')});
            // Also unquoted keys are converted to double-quotes keys for Json compliance
            jsonString = jsonString.replace(/(['"])?([a-zA-Z0-9_]+)(['"])?:/g, '"$2": ');
            return $wnd.JSON.parse(jsonString);
        }
    }-*/;

    @Override
    public final ElementType getNativeElementType(Object nativeElement) {
        switch(getJsType(nativeElement)) {
            case "object": return isArray(nativeElement) ? ElementType.ARRAY : ElementType.OBJECT;
            case "string": return ElementType.STRING;
            case "number": return ElementType.NUMBER;
            case "boolean": return ElementType.BOOLEAN;
        }
        return ElementType.UNKNOWN;
    }

    private static native String getJsType(Object obj) /*-{
        return typeof obj;
    }-*/;

    private static native boolean isArray(Object obj) /*-{
        // ensure that array detection works cross-frame
        return Object.prototype.toString.apply(obj) === '[object Array]';
   }-*/;


    /*@Override
    public final String toJsonString() {
        try {
            return super.toJson();
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode as JSON: " + e.getMessage());
        }
    }*/
}
