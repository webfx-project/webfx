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
package naga.core.spi.json.javaplat;

import naga.core.spi.json.JsonArray;
import naga.core.spi.json.JsonElement;
import naga.core.spi.json.JsonType;

import java.util.List;
import java.util.Map;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/impl/JreJsonElement.java">Original Goodow class</a>
 */
public abstract class JreJsonElement implements JsonElement {
    private static final long serialVersionUID = 3661435771481171596L;

    protected boolean needsCopy;

    @Override
    public boolean isArray() {
        return this instanceof JsonArray;
    }

    @Override
    public boolean isObject() {
        return this instanceof JreJsonObject;
    }

    public abstract <T> Object toNative();

    @Override
    public String toString() {
        return toJsonString();
    }

    protected JsonType getType(Object value) {
        if (value instanceof Map) {
            return JsonType.OBJECT;
        } else if (value instanceof List) {
            return JsonType.ARRAY;
        } else if (value instanceof String) {
            return JsonType.STRING;
        } else if (value instanceof Number) {
            return JsonType.NUMBER;
        } else if (value instanceof Boolean) {
            return JsonType.BOOLEAN;
        } else if (value == null) {
            return JsonType.NULL;
        }
        throw new IllegalArgumentException("Invalid JSON type: " + value.getClass().getName());
    }
}