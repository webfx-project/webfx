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
import naga.core.spi.json.JsonType;

/**
 * Client-side implementation of JsonArray.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonArray.java">Original Goodow class</a>
 */
final class GwtJsonArray extends GwtJsonElement implements JsonArray {

    public static JsonArray create() {
        return (GwtJsonArray) createArray();
    }

    protected GwtJsonArray() {
    }

    @Override
    // @formatter:off
    public native <T> void forEach(ListIterator<T> handler) /*-{
    if (Array.prototype.forEach) {
      Array.prototype.forEach.call(this, function(item, index, array) {
        handler.
        @naga.core.spi.json.JsonArray.ListIterator::call(ILjava/lang/Object;)
        (index, item);
      });
    } else {
      var len = this.length;  // must be fixed during loop...
      for (var i = 0; i < len; i++) {
        handler.
        @naga.core.spi.json.JsonArray.ListIterator::call(ILjava/lang/Object;)
        (i, this[i]);
      }
    }
  }-*/;
    // @formatter:on

    @SuppressWarnings("unchecked")
    @Override
    public GwtJsonElement get(int index) {
        return get0(index).cast();
    }

    @Override
    public GwtJsonArray getArray(int index) {
        return (GwtJsonArray) get(index);
    }

    @Override
    // @formatter:off
    public native boolean getBoolean(int index) /*-{
    return this[index];
  }-*/;

    @Override
    public native double getNumber(int index) /*-{
    return this[index];
  }-*/;
    // @formatter:on

    @Override
    public GwtJsonObject getObject(int index) {
        return (GwtJsonObject) get(index);
    }

    @Override
    // @formatter:off
    public native String getString(int index) /*-{
    return this[index];
  }-*/;
    // @formatter:on

    @Override
    public JsonType getType(int index) {
        return get0(index).getType();
    }

    @Override
    // @formatter:off
    public native int indexOf(Object value) /*-{
    return this.indexOf(value);
  }-*/;

    @Override
    public native JsonArray insert(int index, Object element) /*-{
    this.splice(index, 0, element);
    return this;
  }-*/;

    @Override
    public native int length() /*-{
    return this.length;
  }-*/;

    @Override
    public native JsonArray push(boolean bool_) /*-{
    this[this.length] = bool_;
    return this;
  }-*/;

    @Override
    public native JsonArray push(double number) /*-{
    this[this.length] = number;
    return this;
  }-*/;

    @Override
    public native JsonArray push(Object element) /*-{
    this[this.length] = element;
    return this;
  }-*/;

    @Override
    public native <T> T remove(int index) /*-{
    return this.splice(index, 1)[0];
  }-*/;
    // @formatter:on

    @Override
    public boolean removeValue(Object value) {
        int idx = indexOf(value);
        if (idx == -1) {
            return false;
        }
        remove(idx);
        return true;
    }

    // @formatter:off
    private native GwtJsonValue get0(int index) /*-{
    return this[index];
  }-*/;
    // @formatter:on
}