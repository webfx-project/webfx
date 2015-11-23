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
package naga.core.spi.json;

/**
 * Represents a Json object.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/JsonObject.java">Original Goodow class</a>
 */
public interface JsonObject extends JsonElement {
    /**
     * Callback to support iterating through the values on this object.
     */
    interface MapIterator<T> {
        void call(String key, T value);
    }

    /**
     * Method for iterating through the contents of an object.
     * <p>
     * <p>
     * {@code T} is the expected type of the values returned from the map. <b>Caveat:</b> if you have
     * a map of heterogeneous types, you need to think what value of T you specify here.
     *
     * @param handler The callback object that gets called on each iteration.
     */
    <T> void forEach(MapIterator<T> handler);

    /**
     * Return the element (uncoerced) as a value.
     */
    <T> T get(String key);

    /**
     * Return the element (uncoerced) as a JsonArray. If the type is not an array, this can result in
     * runtime errors.
     */
    JsonArray getArray(String key);

    /**
     * Return the element (uncoerced) as a boolean. If the type is not a boolean, this can result in
     * runtime errors.
     */
    boolean getBoolean(String key);

    /**
     * Return the element (uncoerced) as a number. If the type is not a number, this can result in
     * runtime errors.
     */
    double getNumber(String key);

    /**
     * Return the element (uncoerced) as a JsonObject. If the type is not an object, this can result
     * in runtime errors.
     */
    JsonObject getObject(String key);

    /**
     * Return the element (uncoerced) as a String. If the type is not a String, this can result in
     * runtime errors.
     */
    String getString(String key);

    /**
     * Returns an enumeration representing the fundamental JSON type.
     */
    JsonType getType(String key);

    /**
     * Test whether a given key has present.
     */
    boolean has(String key);

    /**
     * All keys of the object.
     */
    JsonArray keys();

    /**
     * Remove a given key and associated value from the object.
     */
    <T> T remove(String key);

    /**
     * Set a given key to the given boolean value.
     */
    JsonObject set(String key, boolean bool_);

    /**
     * Set a given key to the given double value.
     */
    JsonObject set(String key, double number);

    /**
     * Set a given key to the given value.
     */
    JsonObject set(String key, Object value);

    /**
     * The number of keys in the object.
     */
    int size();
}