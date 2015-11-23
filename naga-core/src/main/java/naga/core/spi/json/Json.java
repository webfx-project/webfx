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
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-json project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-json/blob/master/src/main/java/com/goodow/json/Json.java">Original Goodow class</a>
 */
public class Json {

    public static JsonArray createArray() {
        return getFactory().createArray();
    }

    public static JsonObject createObject() {
        return getFactory().createObject();
    }

    public static <T> T parse(String jsonString) {
        return getFactory().parse(jsonString);
    }

    private static JsonFactory FACTORY;

    public static void registerFactory(JsonFactory jsonFactory) {
         FACTORY = jsonFactory;
    }

    public static JsonFactory getFactory() {
        assert FACTORY != null : "You must register a JSON factory first by invoke Json.registerFactory()";
        return FACTORY;
    }
}