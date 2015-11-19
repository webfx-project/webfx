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

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;
import com.google.gwt.core.client.JsArrayString;

/**
 * Client-side implementation of JsonObject interface.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 * 
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonObject.java">Original Goodow class</a>
 */
public final class GwtJsonObject extends GwtJsonElement implements JsonObject {

    private static final long serialVersionUID = -5864296191395989510L;

    public static GwtJsonObject create() {
        return createObject().cast();
    }

    protected GwtJsonObject() {
    }

    @Override
    // @formatter:off
    public native <T> void forEach(MapIterator<T> handler) /*-{
    for (key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        handler.
        @naga.core.spi.json.JsonObject.MapIterator::call(Ljava/lang/String;Ljava/lang/Object;)
        (key, this[key]);
      }
    }
  }-*/;
    // @formatter:on

    @SuppressWarnings("unchecked")
    @Override
    public GwtJsonElement get(String key) {
        return get0(key).cast();
    }

    @Override
    public GwtJsonArray getArray(String key) {
        return (GwtJsonArray) get(key);
    }

    @Override
    // @formatter:off
    public native boolean getBoolean(String key) /*-{
    return this[key];
  }-*/;

    @Override
    public native double getNumber(String key) /*-{
    return this[key];
  }-*/;
    // @formatter:on

    @Override
    public GwtJsonObject getObject(String key) {
        return (GwtJsonObject) get0(key);
    }

    @Override
    // @formatter:off
    public native String getString(String key) /*-{
    return this[key];
  }-*/;
    // @formatter:on

    @Override
    public JsonType getType(String key) {
        return get0(key).getType();
    }

    @Override
    // @formatter:off
    public native boolean has(String key) /*-{
    return key in this;
  }-*/;
    // @formatter:on

    @Override
    public JsonArray keys() {
        JsArrayString str = keys0();
        return reinterpretCast(str);
    }

    @Override
    // @formatter:off
    public native <T> T remove(String key) /*-{
    toRtn = this[key];
    delete this[key];
    return toRtn;
  }-*/;

    @Override
    public native JsonObject set(String key, boolean bool_) /*-{
    this[key] = bool_;
    return this;
  }-*/;

    @Override
    public native JsonObject set(String key, double number) /*-{
    this[key] = number;
    return this;
  }-*/;

    @Override
    // TODO: We still have problem with "__proto__"
    public native JsonObject set(String key, Object element) /*-{
    this[key] = element;
    return this;
  }-*/;

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

    private native GwtJsonValue get0(String key) /*-{
    return this[key];
  }-*/;

    private native JsArrayString keys0() /*-{
    var keys = [];
    for(var key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key) && key != '$H') {
        keys.push(key);
      }
    }
    return keys;
  }-*/;

    private native JsonArray reinterpretCast(JsArrayString arrayString) /*-{
    return arrayString;
  }-*/;
    // @formatter:on
}