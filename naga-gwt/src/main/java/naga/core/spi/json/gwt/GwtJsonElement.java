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

import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonException;
import naga.core.spi.json.JsonType;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonElement.java">Original Goodow class</a>
 */
abstract class GwtJsonElement extends GwtJsonValue implements JsonElement {

    private static final long serialVersionUID = 2020864649803892593L;

    protected GwtJsonElement() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T extends JsonElement> T clear() {
        return (T) (isObject() ? clearObject() : clearArray());
    }

    @Override
    // @formatter:off
    public final native <T extends JsonElement> T copy() /*-{
    return $wnd.JSON.parse($wnd.JSON.stringify(this));
  }-*/;
    // @formatter:on

    @Override
    public final boolean isArray() {
        return getType() == JsonType.ARRAY;
    }

    @Override
    public final boolean isObject() {
        return getType() == JsonType.OBJECT;
    }

    @Override
    public final String toJsonString() {
        try {
            return super.toJson();
        } catch (Exception e) {
            throw new JsonException("Failed to encode as JSON: " + e.getMessage());
        }
    }

    // @formatter:off
    private native GwtJsonArray clearArray() /*-{
    this.length = 0;
    return this;
  }-*/;

    private native GwtJsonObject clearObject() /*-{
    for (var key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        delete this[key];
      }
    }
    return this;
  }-*/;
    // @formatter:on
}
