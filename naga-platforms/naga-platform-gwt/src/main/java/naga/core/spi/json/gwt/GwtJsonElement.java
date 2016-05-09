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
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonException;
import naga.core.composite.ElementType;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonElement.java">Original Goodow class</a>
 */
abstract class GwtJsonElement extends GwtJsonValue implements JsonElement {

    protected GwtJsonElement() {
    }

    @Override
    public final native GwtJsonElement getNativeElement() /*-{
        return this;
      }-*/;


    @Override
    public final native GwtJsonElement copy() /*-{
        return $wnd.JSON.parse($wnd.JSON.stringify(this));
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
    public final GwtJsonElement createNativeObject() {
        return JavaScriptObject.createObject().cast();
    }

    @Override
    public final GwtJsonElement createNativeArray() {
        return JavaScriptObject.createArray().cast();
    }

    @Override
    public final Object parseNativeObject(String text) {
        return parse0(text);
    }

    @Override
    public final Object parseNativeArray(String text) {
        return parse0(text);
    }

    private native static Object parse0(String jsonString) /*-{
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
            case "string":
            case "number":
            case "boolean": return ElementType.SCALAR;
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


    @Override
    public final String toJsonString() {
        try {
            return super.toJson();
        } catch (Exception e) {
            throw new JsonException("Failed to encode as JSON: " + e.getMessage());
        }
    }
}
