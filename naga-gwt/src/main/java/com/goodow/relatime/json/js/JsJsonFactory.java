/*
 * Note: this code is a fork of Goodow realtime-json project https://github.com/goodow/realtime-json
 */

/*
 * Copyright 2014 Goodow.com
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

import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.json.JsonException;
import com.goodow.realtime.json.JsonFactory;
import com.goodow.realtime.json.JsonObject;

public class JsJsonFactory implements JsonFactory {
  // @formatter:off
  private native static <T> T parse0(String jsonString) /*-{
    // assume Chrome, safe and non-broken JSON.parse impl
    return $wnd.JSON.parse(jsonString);
  }-*/;
  // @formatter:on

  @Override
  public JsonArray createArray() {
    return JsJsonArray.create();
  }

  @Override
  public JsonObject createObject() {
    return JsJsonObject.create();
  }

  @Override
  public <T> T parse(String jsonString) throws JsonException {
    try {
      return parse0(jsonString);
    } catch (Exception e) {
      throw new JsonException("Can't parse JSON string: " + e.getMessage());
    }
  }
}