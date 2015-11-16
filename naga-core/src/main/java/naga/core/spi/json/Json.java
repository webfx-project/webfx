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
 * Vends out implementation of JsonFactory.
 *
 * @author 田传武 (aka larrytin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *         <p>
 *         <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/Json.java">Original Goodow class</a>
 */
public class Json {

    private static JsonFactory FACTORY; // = new JreJsonFactory();

    public static JsonArray createArray() {
        return FACTORY.createArray();
    }

    public static JsonObject createObject() {
        return FACTORY.createObject();
    }

    public static <T> T parse(String jsonString) {
        return FACTORY.parse(jsonString);
    }

    public static void setFactory(JsonFactory factory) {
        FACTORY = factory;
    }

    private Json() {
    }
}