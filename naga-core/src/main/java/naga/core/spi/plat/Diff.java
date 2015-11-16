/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
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
package naga.core.spi.plat;

import naga.core.spi.json.JsonArray;

import java.util.Comparator;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-channel project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-channel/blob/master/src/main/java/com/goodow/realtime/core/Diff.java">Original Goodow class</a>
 */
public interface Diff {

    interface ListTarget<T> {
        void insert(int startIndex, T values);

        void remove(int startIndex, int length);

        void replace(int startIndex, T values);

        void move(int fromIndex, int toIndex, int length);
    }

    void diff(String before, String after, ListTarget<String> target);

    void diff(JsonArray before, JsonArray after, ListTarget<JsonArray> target,
              Comparator<Object> comparator);
}