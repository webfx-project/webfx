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
 * Represents a Json array.
 *
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/Json.java">Original Goodow class</a>
 */
public interface JsonArray extends JsonElement {

    interface ListIterator<T> {
        void call(int index, T value);
    }

    /**
     * Calls a function for each element in an array.
     */
    <T> void forEach(ListIterator<T> handler);

    /**
     * Return the ith element of the array.
     */
    <T> T get(int index);

    /**
     * Return the ith element of the array (uncoerced) as a JsonArray. If the type is not an array,
     * this can result in runtime errors.
     */
    JsonArray getArray(int index);

    /**
     * Return the ith element of the array (uncoerced) as a boolean. If the type is not a boolean,
     * this can result in runtime errors.
     */
    boolean getBoolean(int index);

    /**
     * Return the ith element of the array (uncoerced) as a number. If the type is not a number, this
     * can result in runtime errors.
     */
    double getNumber(int index);

    /**
     * Return the ith element of the array (uncoerced) as a JsonObject If the type is not an object,,
     * this can result in runtime errors.
     */
    JsonObject getObject(int index);

    /**
     * Return the ith element of the array (uncoerced) as a String. If the type is not a String, this
     * can result in runtime errors.
     */
    String getString(int index);

    /**
     * Returns an enumeration representing the fundamental JSON type.
     */
    JsonType getType(int index);

    /**
     * Returns the first index of the given value, or -1 if it cannot be found.
     */
    int indexOf(Object value);

    /**
     * Inserts a new value into the array at the specified index.
     */
    JsonArray insert(int index, Object value);

    /**
     * Length of the array.
     */
    int length();

    /**
     * Pushes the given boolean value onto the end of the array.
     */
    JsonArray push(boolean bool_);

    /**
     * Pushes the given number value onto the end of the array.
     */
    JsonArray push(double number);

    /**
     * Pushes the given value onto the end of the array.
     */
    JsonArray push(Object value);

    /**
     * Remove an element of the array at a particular index.
     */
    <T> T remove(int index);

    /**
     * Removes the first instance of the given value from the list.
     *
     * @return Whether the item was removed.
     */
    boolean removeValue(Object value);
}