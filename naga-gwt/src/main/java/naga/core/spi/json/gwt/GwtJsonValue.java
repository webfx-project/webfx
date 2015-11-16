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

import naga.core.spi.json.JsonType;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * JSO backed implementation of JsonValue.
 *
 * @author 田传武 (aka larrytin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonValue.java">Original Goodow class</a>
 */
class GwtJsonValue extends JavaScriptObject {

    // @formatter:off
    private static native String getJsType(Object obj) /*-{
    return typeof obj;
  }-*/;

    private static native boolean isArray(Object obj) /*-{
     // ensure that array detection works cross-frame
    return Object.prototype.toString.apply(obj) === '[object Array]';
  }-*/;

    private static native boolean isNull(GwtJsonValue gwtJsonValue) /*-{
    // TODO(cromwellian): if this moves to GWT, we may have to support more leniency
    return jsJsonValue === null;
  }-*/;
    // @formatter:on

    protected GwtJsonValue() {
    }

    public final JsonType getType() {
        if (isNull(this)) {
            return JsonType.NULL;
        }
        String jsType = getJsType(this);
        if ("string".equals(jsType)) {
            return JsonType.STRING;
        } else if ("number".equals(jsType)) {
            return JsonType.NUMBER;
        } else if ("boolean".equals(jsType)) {
            return JsonType.BOOLEAN;
        } else if ("object".equals(jsType)) {
            return isArray(this) ? JsonType.ARRAY : JsonType.OBJECT;
        }
        assert false : "Unknown Json Type";
        return null;
    }

    // @formatter:off
    public final native String toJson() /*-{
    // skip hashCode field
    return $wnd.JSON.stringify(this, function(keyName, value) {
        if (keyName == "$H") {
          return undefined; // skip hashCode property
        }
        return value;
      }, 0);
  }-*/;
    // @formatter:on
}