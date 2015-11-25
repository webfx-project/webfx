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
package naga.core.spi.json.gwt;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonException;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonFactory.java">Original Goodow class</a>
 */
public final class GwtJsonFactory implements JsonFactory {

    // @formatter:off
    private native static <T> T parse0(String jsonString) /*-{
    // assume Chrome, safe and non-broken JSON.parse impl
    var obj = $wnd.JSON.parse(jsonString);
    return obj;
  }-*/;
    // @formatter:on

    @Override
    public JsonArray createArray() {
        return GwtJsonArray.create();
    }

    @Override
    public JsonObject createObject() {
        return GwtJsonObject.create();
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