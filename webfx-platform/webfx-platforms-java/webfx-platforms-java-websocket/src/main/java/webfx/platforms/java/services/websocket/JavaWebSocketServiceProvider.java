/*
 * Note: this code is a fork of Goodow realtime-android project https://github.com/goodow/realtime-android
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
package webfx.platforms.java.services.websocket;

import webfx.platforms.core.services.websocket.spi.WebSocketServiceProvider;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.websocket.WebSocket;

/*
 * @author Bruno Salmon
 */
public final class JavaWebSocketServiceProvider implements WebSocketServiceProvider {

    @Override
    public WebSocket createWebSocket(String url, JsonObject options) {
        return new JavaWebSocket(url);
    }
}