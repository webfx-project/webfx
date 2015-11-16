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
package com.goodow.relatime.json.js;

import com.goodow.realtime.json.JsonElement;
import com.goodow.realtime.json.JsonException;
import com.goodow.realtime.json.JsonType;

abstract class JsJsonElement extends JsJsonValue implements JsonElement {
  private static final long serialVersionUID = 2020864649803892593L;

  protected JsJsonElement() {
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
  private native JsJsonArray clearArray() /*-{
    this.length = 0;
    return this;
  }-*/;

  private native JsJsonObject clearObject() /*-{
    for (var key in this) {
      if (Object.prototype.hasOwnProperty.call(this, key)) {
        delete this[key];
      }
    }
    return this;
  }-*/;
  // @formatter:on
}
