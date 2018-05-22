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
package naga.providers.platform.abstr.java.services.websocket;

import naga.platform.services.websocket.WebSocket;
import naga.platform.services.websocket.WebSocketListener;
import naga.platform.services.json.Json;
import naga.platform.services.json.WritableJsonObject;
import naga.platform.services.log.Logger;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/*
 * @author 田传武 (aka Larry Tin) - author of Goodow realtime-android project
 * @author Bruno Salmon - fork, refactor & update for the naga project
 *
 * <a href="https://github.com/goodow/realtime-android/blob/master/src/main/java/com/goodow/realtime/core/WebSocket.java">Original Goodow class</a>
 */
public final class JavaWebSocket implements WebSocket {
    private static Charset charset = Charset.forName("UTF-8");
    private static CharsetDecoder decoder = charset.newDecoder();

    private static String toString(ByteBuffer buffer) throws CharacterCodingException {
        int old_position = buffer.position();
        String data = decoder.decode(buffer).toString();
        // reset buffer's position to its original so it is not altered:
        buffer.position(old_position);
        return data;
    }

    private WebSocketClient socket;
    private WebSocketListener listener;

    public JavaWebSocket(String uri) {
        URI serverUri;
        try {
            serverUri = new URI(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket = new WebSocketClient(serverUri, new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                if (listener != null)
                    listener.onOpen();
            }

            @Override
            public void onMessage(String msg) {
                if (listener != null)
                    listener.onMessage(msg);
            }

            @Override
            public void onMessage(ByteBuffer buffer) {
                try {
                    if (listener != null)
                        listener.onMessage(JavaWebSocket.toString(buffer));
                } catch (CharacterCodingException e) {
                    Logger.log("Websocket Failed when Charset Decoding", e);
                }
            }

            @Override
            public void onError(Exception e) {
                if (listener != null) {
                    String message = e.getMessage();
                    listener.onError(message == null ? e.getClass().getSimpleName() : message);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                if (listener != null) {
                    // listener.onClose(Json.createObject().set("code", code).set("reason", reason).set("remote", remote)); // Doesn't compile in Maven since naga-noreflect is a separate module for any strange reason
                    WritableJsonObject jsonObject = Json.createObject();
                    jsonObject.set("code", code);
                    jsonObject.set("reason", reason);
                    jsonObject.set("remote", remote);
                    listener.onClose(jsonObject);
                }
            }
        };

        socket.connect();
    }

    @Override
    public State getReadyState() {
        READYSTATE readyState = socket.getReadyState();
        return readyState == READYSTATE.NOT_YET_CONNECTED ? State.CONNECTING : State.values[readyState.ordinal() - 1];
    }

    @Override
    public void send(String data) {
        try {
            //log.finest("Websocket send: " + data);
            socket.getConnection().send(data);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setListener(WebSocketListener listener) {
        this.listener = listener;
    }


    @Override
    public void close() {
        socket.close();
    }
}