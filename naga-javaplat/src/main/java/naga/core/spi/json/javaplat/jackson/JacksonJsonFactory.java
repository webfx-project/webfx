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
package naga.core.spi.json.javaplat.jackson;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.JsonObject;

import java.util.List;
import java.util.Map;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/impl/JreJsonFactory.java">Original Goodow class</a>
 */
public final class JacksonJsonFactory implements JsonFactory {
    @Override
    public JsonArray createArray() {
        return new JacksonJsonArray();
    }

    @Override
    public JsonObject createObject() {
        return new JacksonJsonObject();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parse(String jsonString) {
        Object value = JacksonUtil.decodeValue(jsonString, Object.class);
        if (value instanceof Map)
            return (T) new JacksonJsonObject((Map<String, Object>) value);
        if (value instanceof List)
            return (T) new JacksonJsonArray((List<Object>) value);
        return (T) value;
    }
}