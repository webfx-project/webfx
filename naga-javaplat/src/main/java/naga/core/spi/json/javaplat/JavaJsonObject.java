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
import naga.core.spi.json.JsonObject;
import naga.core.spi.json.JsonType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Server-side implementation of JsonObject.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/impl/JreJsonObject.java">Original Goodow class</a>
 */
public class JavaJsonObject extends JavaJsonElement implements JsonObject {
    private static final long serialVersionUID = -2848796364089017455L;

    @SuppressWarnings("unchecked")
    static Map<String, Object> convertMap(Map<String, Object> map) {
        Map<String, Object> converted = new LinkedHashMap<String, Object>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                Map<String, Object> jm = (Map<String, Object>) obj;
                converted.put(entry.getKey(), convertMap(jm));
            } else if (obj instanceof List) {
                List<Object> list = (List<Object>) obj;
                converted.put(entry.getKey(), JavaJsonArray.convertList(list));
            } else
                converted.put(entry.getKey(), obj);
        }
        return converted;
    }

    protected Map<String, Object> map;

    public JavaJsonObject() {
        this.map = new LinkedHashMap<>();
    }

    public JavaJsonObject(Map<String, Object> map) {
        this.map = map;
    }

  /*
  public JreJsonObject(String jsonString) {
    map = JacksonUtil.decodeValue(jsonString, Map.class);
  }
  */

    @SuppressWarnings("unchecked")
    @Override
    public JsonObject clear() {
        if (needsCopy) {
            map = new LinkedHashMap<>();
            needsCopy = false;
        } else
            map.clear();
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonObject copy() {
        JavaJsonObject copy = new JavaJsonObject(map);
        // We actually do the copy lazily if the object is subsequently mutated
        copy.needsCopy = true;
        needsCopy = true;
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        JavaJsonObject that = (JavaJsonObject) o;

        if (this.map.size() != that.map.size())
            return false;

        for (Map.Entry<String, Object> entry : this.map.entrySet()) {
            Object val = entry.getValue();
            if (val == null) {
                if (that.map.get(entry.getKey()) != null)
                    return false;
            } else if (!entry.getValue().equals(that.map.get(entry.getKey())))
                    return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void forEach(MapIterator<T> handler) {
        for (String key : map.keySet())
            handler.call(key, get(key));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> T get(String key) {
        Object value = map.get(key);
        if (value instanceof Map)
            value = new JavaJsonObject((Map) value);
        else if (value instanceof List)
            value = new JavaJsonArray((List) value);
        return (T) value;
    }

    @Override
    public JavaJsonArray getArray(String key) {
        @SuppressWarnings("unchecked")
        List<Object> l = (List<Object>) map.get(key);
        return l == null ? null : new JavaJsonArray(l);
    }

    @Override
    public boolean getBoolean(String key) {
        return (Boolean) map.get(key);
    }

    @Override
    public double getNumber(String key) {
        return ((Number) map.get(key)).doubleValue();
    }

    @Override
    public JavaJsonObject getObject(String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> m = (Map<String, Object>) map.get(key);
        return m == null ? null : new JavaJsonObject(m);
    }

    @Override
    public String getString(String key) {
        return (String) map.get(key);
    }

    @Override
    public JsonType getType(String key) {
        return super.getType(map.get(key));
    }

    @Override
    public boolean has(String key) {
        return map.containsKey(key);
    }

    @Override
    public JsonArray keys() {
        return new JavaJsonArray(Arrays.asList(map.keySet().toArray(new Object[map.size()])));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T remove(String key) {
        checkCopy();
        return (T) map.remove(key);
    }

    @Override
    public JsonObject set(String key, boolean bool_) {
        checkCopy();
        map.put(key, bool_);
        return this;
    }

    @Override
    public JsonObject set(String key, double number) {
        checkCopy();
        map.put(key, number);
        return this;
    }

    @Override
    public JsonObject set(String key, Object value) {
        checkCopy();
        if (value instanceof JavaJsonObject) {
            value = ((JavaJsonObject) value).map;
        } else if (value instanceof JavaJsonArray) {
            value = ((JavaJsonArray) value).list;
        }
        map.put(key, value);
        return this;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public String toJsonString() {
        return JacksonUtil.encode(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> toNative() {
        return (Map<String, T>) map;
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    private void checkCopy() {
        if (needsCopy) {
            // deep copy the map lazily if the object is mutated
            map = convertMap(map);
            needsCopy = false;
        }
    }
}