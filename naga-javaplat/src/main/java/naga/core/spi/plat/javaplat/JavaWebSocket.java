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
package naga.core.spi.plat.javaplat;

import naga.core.spi.bus.State;
import naga.core.spi.json.Json;
import naga.core.spi.plat.WebSocket;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @author 田传武 (aka larrytin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public class JavaWebSocket implements WebSocket {
    private static final Logger log = Logger.getLogger(JavaWebSocket.class.getName());
    private static Charset charset = Charset.forName("UTF-8");
    private static CharsetDecoder decoder = charset.newDecoder();

    private static String toString(ByteBuffer buffer) throws CharacterCodingException {
        String data = null;
        int old_position = buffer.position();
        data = decoder.decode(buffer).toString();
        // reset buffer's position to its original so it is not altered:
        buffer.position(old_position);
        return data;
    }

    private WebSocketClient socket;
    private WebSocketHandler eventHandler;

    public JavaWebSocket(String uri) {
        URI serverUri = null;
        try {
            serverUri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket = new WebSocketClient(serverUri, new Draft_17()) {
            @Override
            public void onClose(final int code, final String reason, final boolean remote) {
                log.info("WebSocket closed, code=" + code + ", reason=" + reason + ", remote=" + remote);
                if (eventHandler == null) {
                    return;
                }
                eventHandler.onClose(Json.createObject().set("code", code).set("reason", reason).set(
                        "remote", remote));
            }

            @Override
            public void onError(final Exception e) {
                log.log(Level.SEVERE, "Websocket Failed With Exception", e);
                if (eventHandler == null) {
                    return;
                }
                String message = e.getMessage();
                eventHandler.onError(message == null ? e.getClass().getSimpleName() : message);
            }

            @Override
            public void onMessage(final ByteBuffer buffer) {
                final String msg;
                try {
                    msg = JavaWebSocket.toString(buffer);
                } catch (CharacterCodingException e) {
                    log.log(Level.SEVERE, "Websocket Failed when Charset Decoding", e);
                    return;
                }
                log.finest("Websocket Received: " + msg);
                if (eventHandler == null) {
                    return;
                }
                eventHandler.onMessage(msg);
            }

            @Override
            public void onMessage(final String msg) {
                log.finest("Websocket received: " + msg);
                if (eventHandler == null) {
                    return;
                }
                eventHandler.onMessage(msg);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                log.info("Websocket Connected");
                if (eventHandler == null) {
                    return;
                }
                eventHandler.onOpen();
            }
        };

        socket.connect();
    }

    @Override
    public void close() {
        socket.close();
    }

    @Override
    public State getReadyState() {
        READYSTATE readyState = socket.getReadyState();
        return readyState == READYSTATE.NOT_YET_CONNECTED ? State.CONNECTING : State.values[readyState
                .ordinal() - 1];
    }

    @Override
    public void send(String data) {
        try {
            log.finest("Websocket send: " + data);
            socket.getConnection().send(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setListen(WebSocketHandler handler) {
        this.eventHandler = handler;
    }
}