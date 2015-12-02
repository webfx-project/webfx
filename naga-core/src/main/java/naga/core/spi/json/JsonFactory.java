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
package naga.core.spi.json;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/JsonFactory.java">Original Goodow class</a>
 */
public abstract class JsonFactory<NA, NO> {

    public abstract JsonArray createArray();

    public JsonArray createArray(NA nativeArray) {
        return isNativeValueSet(nativeArray) ? createNewArray(nativeArray) : null;
    }

    protected abstract JsonArray createNewArray(NA nativeArray);

    public abstract JsonObject createObject();

    public JsonObject createObject(NO nativeObject) {
        return isNativeValueSet(nativeObject) ? createNewObject(nativeObject) : null;
    }

    protected abstract JsonObject createNewObject(NO nativeObject);

    protected boolean isNativeValueSet(Object nativeValue) {
        return nativeValue != null;
    }

    public <T extends JsonElement> T parse(String jsonString) {
        return nativeToJsonElement(parseNative(jsonString));
    }

    protected abstract Object parseNative(String jsonString);

    protected abstract <T extends JsonElement> T nativeToJsonElement(Object nativeElement);
}