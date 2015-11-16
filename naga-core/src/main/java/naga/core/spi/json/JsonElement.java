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

import java.io.Serializable;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/JsonElement.java">Original Goodow class</a>
 */
public interface JsonElement extends Serializable {
    /**
     * Removes all entries.
     */
    <T extends JsonElement> T clear();

    <T extends JsonElement> T copy();

    boolean isArray();

    boolean isObject();

    /**
     * Returns a serialized JSON string representing this value.
     */
    String toJsonString();
}
