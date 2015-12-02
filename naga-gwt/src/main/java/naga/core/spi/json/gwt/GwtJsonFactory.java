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

import com.google.gwt.core.client.JavaScriptObject;
import naga.core.spi.json.*;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/tree/master/src/main/java/com/goodow/realtime/json/js/JsJsonFactory.java">Original Goodow class</a>
 */
public final class GwtJsonFactory extends JsonFactory<GwtJsonArray, GwtJsonObject> {

    @Override
    public GwtJsonArray createArray() {
        return JavaScriptObject.createArray().cast();
    }

    @Override
    public GwtJsonArray createArray(GwtJsonArray gwtArray) {
        return gwtArray;
    }

    @Override
    protected GwtJsonArray createNewArray(GwtJsonArray gwtArray) {
        return gwtArray;
    }

    @Override
    public GwtJsonObject createObject() {
        return JavaScriptObject.createObject().cast();
    }

    @Override
    public GwtJsonObject createObject(GwtJsonObject gwtObject) {
        return gwtObject;
    }

    @Override
    protected JsonObject createNewObject(GwtJsonObject gwtObject) {
        return gwtObject;
    }

    @Override
    protected Object parseNative(String jsonString) {
        return parse0(jsonString);
    }

    // @formatter:off
    private native static Object parse0(String jsonString) /*-{
        // assume Chrome, safe and non-broken JSON.parse impl
        return $wnd.JSON.parse(jsonString);
    }-*/;
    // @formatter:on


    @Override
    protected <T extends JsonElement> T nativeToJsonElement(Object gwtElement) {
        return (T) gwtElement;
    }
}