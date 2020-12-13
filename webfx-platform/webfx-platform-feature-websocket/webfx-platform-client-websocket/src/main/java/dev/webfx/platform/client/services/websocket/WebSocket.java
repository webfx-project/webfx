/*
 * Note: this code is a fork of Goodow realtime-channel project https://github.com/goodow/realtime-channel
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
package dev.webfx.platform.client.services.websocket;

/*
 * @author Bruno Salmon
 */
public interface WebSocket {

    enum State {
        CONNECTING, OPEN, CLOSING, CLOSED;
    }

    State getReadyState();

    void send(String data);

    void setListener(WebSocketListener listener);

    /**
     * Close the socket. The socket cannot be used again after calling close; the server must create a
     * new socket.
     */
    void close();
}