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

/**
 * JSO backed implementation of JsonValue.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonValue.java">Original Goodow class</a>
 */
class GwtJsonValue extends JavaScriptObject {

    protected GwtJsonValue() {
    }

    public final native String toJson() /*-{
        // skip hashCode field
        return $wnd.JSON.stringify(this, function(keyName, value) {
            if (keyName == "$H") {
              return undefined; // skip hashCode property
            }
            return value;
          }, 0);
    }-*/;
}